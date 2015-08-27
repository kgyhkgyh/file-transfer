package src.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import src.protocal.RemotingCommand;
import src.util.Pair;
import src.util.SemaphoreReleaseOnlyOnce;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/26.
 */
public abstract class NettyRemotingAbstract {

    private final ConcurrentHashMap<Integer, RequestFuture> requestTable = new ConcurrentHashMap<>(256);

    private Semaphore semaphoreAsync = new Semaphore(2048);

    private Semaphore semaphoreOneway = new Semaphore(2048);

    protected final HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>> processorTable = new HashMap<>();

    public abstract void registProcessor(int requestCode, NettyRequestProcessor processor, ExecutorService executorService);


    /**
     * 处理netty消息
     * @param ctx
     * @param command
     */
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

    /**
     * 处理请求消息
     * @param ctx
     * @param command
     */
    public void processRequest(final ChannelHandlerContext ctx, RemotingCommand command) {

        if(command.getOpaque() < 2) {
            System.out.println("消息拒收");
            return;
        }
        final Pair<NettyRequestProcessor, ExecutorService> pair = processorTable.get(command.getCmdCode());

        if(pair != null) {
            //创建任务
            Runnable runnable = () -> {
                try {
                    //拿到处理器进行处理
                    RemotingCommand response = pair.getObject1().processRequest(ctx, command);

                    if(!command.isRpcOneway()) {
                        if(response != null) {
                            response.setOpaque(command.getOpaque());
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
     * 处理应答消息
     * @param ctx
     * @param command
     */
    public void processResponse(final ChannelHandlerContext ctx, RemotingCommand command) {
        RequestFuture requestFuture = requestTable.get(command.getOpaque());
        requestFuture.putResponse(command);
        requestTable.remove(command.getOpaque());
        System.out.println(command.getContent());
    }

    /**
     * 异步递送消息，并保证得到应答
     * @param channel
     * @param request
     */
    public void invokeAsync(final Channel channel, final RemotingCommand request, long timeoutMills) throws InterruptedException {
        boolean acquire = semaphoreAsync.tryAcquire(timeoutMills, TimeUnit.MILLISECONDS);
        if(acquire) {
            SemaphoreReleaseOnlyOnce once = new SemaphoreReleaseOnlyOnce(this.semaphoreAsync);
            final RequestFuture requestFuture = RequestFuture.createAsynFuture(request.getOpaque(), timeoutMills, true, once, channel);
            try {
                requestTable.put(request.getOpaque(), requestFuture);
                channel.writeAndFlush(request).addListener((channelFuture) ->{
                    if(channelFuture.isSuccess()) {
                        requestFuture.putRequest(request);
                        return;
                    }else {
                        requestFuture.release();
                        requestTable.remove(request.getOpaque());
                    }
                });
            }catch (Exception e) {
                requestFuture.release();
                requestTable.remove(request.getOpaque());
                e.printStackTrace();
            }
        }else {
            //TODO 超时处理
        }
    }

    /**
     * 同步递送消息
     * @param channel
     * @param request
     * @return
     * @throws Exception
     */
    public RemotingCommand invokeSync(final Channel channel, final RemotingCommand request, long timeoutMills) throws Exception{
        final RequestFuture requestFuture = RequestFuture.createSyncFuture(request.getOpaque(), timeoutMills);
        System.out.println("发送消息:"+request.getOpaque() + "消息内容:" + request.getContent());
        try {
            channel.writeAndFlush(request).addListener((channelFuture) ->{
                if(channelFuture.isSuccess()) {
                    requestTable.put(request.getOpaque(), requestFuture);
                    requestFuture.putRequest(request);
                    requestFuture.setSendRequestOK(true);
                    return;
                }else {
                    requestFuture.setSendRequestOK(false);
                    return;
                }
            });

            RemotingCommand response = null;
            if(requestFuture.isSendRequestOK()) {
                try {
                    response = requestFuture.waitResponse(10 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                //TODO 将任务放入线程池
                response = new RemotingCommand(1, 1, true);
            }

            return response;
        } finally {

        }
    }

    /**
     * 单向递送消息
     * @param channel
     * @param request
     */
    public void invokeOneway(final Channel channel, final RemotingCommand request, long timeoutMills) throws InterruptedException {
        boolean acquire = semaphoreOneway.tryAcquire(timeoutMills, TimeUnit.MILLISECONDS);
        if(acquire) {
            try {
                channel.writeAndFlush(request);
            }catch (Exception e) {

            }
        }
    }

    /**
     * 异步消息超时重发
     */
    public void scanForReSend() {
        Collection<RequestFuture> values = requestTable.values();

        for(Iterator<RequestFuture> iter = values.iterator() ; iter.hasNext(); ) {
            RequestFuture requestFuture = iter.next();
            if(requestFuture.isTimeout()) {

                RemotingCommand requestCommand = requestFuture.getRequestCommand();
                iter.remove();
                if(requestFuture.isReSendRequest()) {
                    try {
                        invokeAsync(requestFuture.getChannel(), requestCommand, requestFuture.getTimeoutMills());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}
