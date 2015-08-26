/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-2-6   comment
 * linxueqin  2015-2-6  Created
 */
package com.apusic.adxp.agent.netty.adxp.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.apusic.adxp.agent.netty.adxp.Host;
import com.apusic.adxp.agent.netty.adxp.HostService;
import com.apusic.adxp.agent.netty.adxp.TransferListener;
import com.apusic.adxp.agent.netty.adxp.TransferServer;

/**
 * @author linxueqin
 * 
 */
public class TransferServerImpl extends ChannelInitializer<SocketChannel> implements TransferServer,
                                                                         TransferHandlerListener, Runnable {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(TransferServerImpl.class);

    /**
     * 服务器编码
     */
    private String code;

    /**
     * 交换监听器
     */
    private TransferListener transferListener = null;

    /**
     * netty服务模块
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * netty客户模块
     */
    private Bootstrap bootstrap = new Bootstrap();
    private EventLoopGroup clientWorkerGroup = new NioEventLoopGroup();

    /**
     * 主机服务
     */
    private HostService hostService;

    /**
     * 运行状态
     */
    private transient boolean running = false;

    /* (non-Javadoc)
     * @see com.apusic.adxp.TransferServer#start(java.lang.String, int, com.apusic.adxp.TransferListener)
     */
    public void start(String code, int port, TransferListener transferListener) throws Exception {
        this.code = code;
        this.transferListener = transferListener;
        this.running = true;

        // 服务器启动
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(this).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

        b.bind(port).sync();

        // 初始化待连接
        bootstrap.group(clientWorkerGroup); // (2)
        bootstrap.channel(NioSocketChannel.class); // (3)
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        bootstrap.handler(this);

        // 启动发送线程
        new Thread(this, code).start();
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.TransferServer#stop()
     */
    public void stop() throws Exception {
        running = false;
        clientWorkerGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.impl.TransferHandlerListener#active(com.apusic.adxp.impl.TransferHandler)
     */
    public void active(TransferHandler transferHandler) throws Exception {
        String key = transferHandler.getCode();
        MsgQueue queue = MsgQueue.createMsgQueue(key);
        queue.bind(transferHandler);
        if (transferListener != null) {
            transferListener.active(transferHandler);
        }
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.impl.TransferHandlerListener#inActive(com.apusic.adxp.impl.TransferHandler)
     */
    public void inActive(TransferHandler transferHandler) throws Exception {
        String key = transferHandler.getCode();
        if (MsgQueue.remove(key) != null && transferListener != null) {
            transferListener.inActive(transferHandler);
        }
    }

    /* (non-Javadoc)
     * @see com.apusic.adxp.impl.TransferHandlerListener#receiveMsg(com.apusic.adxp.impl.TransferHandler, java.lang.Object)
     */
    public void receiveMsg(TransferHandler transferHandler, Object msg) throws Exception {
        if (transferListener != null) {
            transferListener.receiveMsg(transferHandler, msg);
        }
    }

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    /* (non-Javadoc)
     * @see com.apusic.adxp.TransferServer#send(java.lang.String, java.lang.Object)
     */
    public boolean send(String targetCode, Object msg) throws Exception {
        MsgQueue msgQueue = MsgQueue.createMsgQueue(targetCode, new MsgQueue.QueueCreateCallback() {
            
            public boolean onCreate(String targetCode) {
                return connect(targetCode);
            }
        });
        if(msgQueue == null) {
            return false;
        }
        msgQueue.putMsg(msg);
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (running) {
            MsgQueue.work();
        }
    }

    /**
     * 
     * @param targetCode
     */
    private boolean connect(String targetCode) {
        Host host = hostService.findHost(targetCode);
        if (host != null) {
            bootstrap.connect(host.getIp(), host.getPort());
            return true;
        } else {
            if (logger.isErrorEnabled()) {
                logger.error("找不到主机 " + targetCode);
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
     */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(
        // new IdleStateHandler(60, 15, 13,TimeUnit.SECONDS),
        new ObjectEncoder(), new ObjectDecoder(new ClassResolver() {
            public Class<?> resolve(String className) throws ClassNotFoundException {
                return this.getClass().getClassLoader().loadClass(className);
            }
        }), new TransferHandler(code, this));
    }

    /**
     * @param hostService the hostService to set
     */
    public void setHostService(HostService hostService) {
        this.hostService = hostService;
    }

}
