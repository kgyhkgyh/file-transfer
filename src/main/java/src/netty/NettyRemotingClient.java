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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2015/8/25.
 */
public class NettyRemotingClient extends NettyRemotingAbstract implements RemotingClient {

    private final Bootstrap bootstrap;

    private final EventLoopGroup eventWorkerLoopGroup;

    private String ip;

    private int port;

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
    }

    /**
     * 建立channel
     * @param addr
     * @return
     */
    public Channel createChannel(String addr) {
        Channel channel = null;
        if(channelMap.contains(addr)) {
            ChannelFuture channelFuture = channelMap.get(addr);
            channel = channelFuture.channel();
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

    public RemotingCommand invokeSync(String addr, RemotingCommand command) {
        try {
            Channel channel = createChannel(addr);
            if (channel != null && channel.isActive()) {
                RemotingCommand repsonse = this.invokeSync(channel, command);
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

    public void invokeAsync(String addr, RemotingCommand command) {
        try {
            Channel channel = createChannel(addr);
            if (channel != null && channel.isActive()) {
                this.invokeAsync(channel, command);
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

    class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand>{

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RemotingCommand remotingCommand) throws Exception {
            System.out.println("消息的类型:" + remotingCommand.getRpcType() + "  消息的内容:" + remotingCommand.getContent());
        }
    }
}
