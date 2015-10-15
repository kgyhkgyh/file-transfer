package src.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import src.transport.processor.FileTaskProcessor;
import src.transport.protocal.CommandBody;
import src.transport.protocal.FileSegmentRequest;
import src.transport.protocal.RemotingCommand;
import src.transport.RemotingServer;
import src.transport.util.Pair;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2015/8/25.
 */
public class NettyRemotingServer extends NettyRemotingAbstract implements RemotingServer {

    private int port;

    private ServerBootstrap serverBootstrap;

    private final EventLoopGroup eventLoopGroupWorker;

    private final EventLoopGroup eventLoopGroupBoss;

    private final String baseDir;

    private ExecutorService callBackExecutor;

    public NettyRemotingServer(int port, String baseDir) {
        this.port = port;
        this.baseDir = baseDir;
        this.serverBootstrap = new ServerBootstrap();
        this.eventLoopGroupWorker = new NioEventLoopGroup();
        this.eventLoopGroupBoss = new NioEventLoopGroup();
    }

    public void start() {
        ServerBootstrap server = this.serverBootstrap
                .group(eventLoopGroupBoss,eventLoopGroupWorker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(
                            new ObjectEncoder(),
                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                            new NettyServerHandler()
                        );
                    }

                });

        try {
            ChannelFuture sync = server.bind(new InetSocketAddress(this.port)).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException("this.serverBootstrap.bind().sync() InterruptedException", e);
        }

        ExecutorService es = Executors.newCachedThreadPool();
//        this.registProcessor(1, new DefaultProcessor(), es);
        FileTaskProcessor processor = new FileTaskProcessor(baseDir);
        this.registProcessor(1, processor, es);
        this.registProcessor(2, processor, es);
    }

    public void stop(){

        this.eventLoopGroupBoss.shutdownGracefully();

        this.eventLoopGroupWorker.shutdownGracefully();
    }

    @Override
    public void registProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executorService) {
        Pair<NettyRequestProcessor, ExecutorService> pair = new Pair<>(processor, executorService);
        this.processorTable.put(requestCode, pair);
    }

    @Override
    public ExecutorService getExecutor() {
        return this.callBackExecutor;
    }

    class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemotingCommand remotingCommand) throws Exception {
//            String content = remotingCommand.getContent();
//            System.out.println("消息的类型:" + remotingCommand.getRpcType() + "  消息的内容:" + content);
//            RemotingCommand response = new RemotingCommand();
//            response.setType("receive");
//            response.setContent("receive"+content);
//            channelHandlerContext.writeAndFlush(response);
            CommandBody body = remotingCommand.getBody();
            if(body instanceof FileSegmentRequest) {
                FileSegmentRequest f = (FileSegmentRequest) body;
                System.out.println(f.getPosition() +"---" + f.getBlockSize()+"---" + f.getFileName()+ "---"+Arrays.toString(f.getContent()));
            }
            processCommand(channelHandlerContext, remotingCommand);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("connect established");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            System.out.println("connect abort");
        }
    }
}
