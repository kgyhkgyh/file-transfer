package src.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import src.RemotingCommand;
import src.util.Pair;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Created by Administrator on 2015/8/26.
 */
public abstract class NettyRemotingAbstract {

    private final ConcurrentHashMap<Integer, RequestFuture> requestTable = new ConcurrentHashMap<>(16);

    private final HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>> processorTable = new HashMap<>();

    public void processCommand(final ChannelHandlerContext ctx, RemotingCommand command) {
        final RemotingCommand cmd = command;
        if (cmd != null) {
            switch (cmd.getRpcType()) {
                case RemotingCommand.REQUEST_COMMAND:
                    processRequest(ctx, cmd);
                    break;
                case RemotingCommand.RESPONSE_COMMAND:
                    processResponse(ctx, cmd);
                    break;
                default:
                    break;
            }
        }

    }


    public void processRequest(final ChannelHandlerContext ctx, RemotingCommand command) {
        final Pair<NettyRequestProcessor, ExecutorService> pair = processorTable.get(command.getCmdCode());

        if(pair != null) {
            //创建任务
            Runnable runnable = () -> {
                try {
                    //拿到处理器进行处理
                    RemotingCommand response = pair.getObject1().processRequest(ctx, command);

                    if(!command.isRpcOneway()) {
                        if(response != null) {
                            ctx.writeAndFlush(response);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };

            try {
                //将任务放入线程池
                pair.getObject2().submit(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //TODO无法处理的请求
        }
    }

    /**
     * 处理回应
     * @param ctx
     * @param command
     */
    public void processResponse(final ChannelHandlerContext ctx, RemotingCommand command) {
        requestTable.remove(command.getOpaque());
    }

    public void invokeAsync(final Channel channel, final RemotingCommand request) {
        final RequestFuture requestFuture = new RequestFuture(request.getOpaque(), 60 * 1000, true);
        try {
            requestTable.put(request.getOpaque(), requestFuture);
            channel.writeAndFlush(request).addListener((channelFuture) ->{
                if(channelFuture.isSuccess()) {
                    requestFuture.putRequest(request);
                    return;
                }else {
                    requestTable.remove(request.getOpaque());
                }
            });
        }catch (Exception e) {

        }
    }

}
