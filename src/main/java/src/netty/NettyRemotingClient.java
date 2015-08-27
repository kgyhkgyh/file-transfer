package src.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import src.RemotingClient;
import src.protocal.RemotingCommand;
import src.processor.DefaultProcessor;
import src.util.Pair;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/8/25.
 */
public class NettyRemotingClient extends NettyRemotingAbstract implements RemotingClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventWorkerLoopGroup;

    private ConcurrentHashMap<String, ChannelFuture> channelMap = new ConcurrentHashMap<>();

    public NettyRemotingClient() {
        this.bootstrap = new Bootstrap();
        this.eventWorkerLoopGroup = new NioEventLoopGroup();
    }

    public void start() {
        this.bootstrap
                .group(eventWorkerLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                                new ObjectEncoder(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new NettyClientHandler()
                        );
                    }

                });
        ExecutorService es = Executors.newCachedThreadPool();
        this.registProcessor(1, new DefaultProcessor(), es);
    }

    /**
     * 建立channel
     * @param addr
     * @return
     */
    public Channel createChannel(String addr) {
        Channel channel = null;
        ChannelFuture future = channelMap.get(addr);
        if(future != null) {
            channel = future.channel();
            return channel;
        }

        String[] split = addr.split(":");
        InetSocketAddress address = new InetSocketAddress(split[0], Integer.valueOf(split[1]));
        ChannelFuture sync = null;
        try {
            sync = this.bootstrap.connect(address).sync();
            channelMap.put(addr, sync);
            channel = sync.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return channel;
    }

    /**
     * 关闭channel
     * @param addr
     * @param channel
     */
    public void closeChannel(String addr, Channel channel) {

    }

    public RemotingCommand invokeSync(String addr, RemotingCommand command, long timeoutMills) {
        try {
            Channel channel = createChannel(addr);
            if (channel != null && channel.isActive()) {
                RemotingCommand repsonse = this.invokeSync(channel, command, timeoutMills);
                return repsonse;
            }else {
                this.closeChannel(addr, channel);
                throw new Exception(addr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void invokeAsync(String addr, RemotingCommand command, long timeoutMills) {
        try {
            Channel channel = createChannel(addr);
            if (channel != null && channel.isActive()) {
                this.invokeAsync(channel, command, timeoutMills);
            }else {
                this.closeChannel(addr, channel);
                throw new Exception(addr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void invokeOneway(String addr, RemotingCommand command, long timeoutMills) {
        try {
            Channel channel = createChannel(addr);
            if (channel != null && channel.isActive()) {
                this.invokeOneway(channel, command, timeoutMills);
            }else {
                this.closeChannel(addr, channel);
                throw new Exception(addr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.eventWorkerLoopGroup.shutdownGracefully();
    }

    @Override
    public void registProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executorService) {
        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<>(processor, executorService);
        this.processorTable.put(requestCode, pair);
    }

    class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemotingCommand remotingCommand) throws Exception {
            processCommand(channelHandlerContext, remotingCommand);
        }
    }
}
