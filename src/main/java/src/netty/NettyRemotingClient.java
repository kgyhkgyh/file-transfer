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
import src.RemotingCommand;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2015/8/25.
 */
public class NettyRemotingClient implements RemotingClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventWorkerLoopGroup;

    private String ip;

    private int port;

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
    }

    public void invokeSync(String addr, RemotingCommand command) {
        String[] split = addr.split(":");
        InetSocketAddress address = new InetSocketAddress(split[0], Integer.valueOf(split[1]));
        ChannelFuture sync = null;
        try {
            sync = this.bootstrap.connect(address).sync();
            Channel channel = sync.channel();
            channel.writeAndFlush(command);

            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.eventWorkerLoopGroup.shutdownGracefully();
    }

    class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemotingCommand remotingCommand) throws Exception {
            System.out.println("消息的类型:" + remotingCommand.getRpcType() + "  消息的内容:" + remotingCommand.getContent());
        }
    }
}
