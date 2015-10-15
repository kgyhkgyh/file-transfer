package src.transport.netty;

import io.netty.channel.Channel;
import src.transport.CommandCallBack;
import src.transport.protocal.RemotingCommand;
import src.transport.util.SemaphoreReleaseOnlyOnce;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/8/26.
 */
public class RequestFuture {

    private volatile RemotingCommand requestCommand;

    private volatile RemotingCommand responseCommand;

    private boolean sendRequestOK = true;

    private CommandCallBack callBack;

    private final int opaque;

    private final long timeoutMills;

    private long beginTimestamp = System.currentTimeMillis();

    private CountDownLatch latch = new CountDownLatch(1);

    private final boolean reSendRequest;

    private final SemaphoreReleaseOnlyOnce once;

    private Channel channel;

    public RequestFuture(int opaque, long timeoutMills, boolean reSendRequest, SemaphoreReleaseOnlyOnce once, Channel channel, CommandCallBack callBack) {
        this.opaque = opaque;
        this.timeoutMills = timeoutMills;
        this.reSendRequest = reSendRequest;
        this.once = once;
        this.channel = channel;
        this.callBack = callBack;
    }

    public static RequestFuture createSyncFuture(int opaque, long timeoutMills) {
        return new RequestFuture(opaque, timeoutMills, false, null, null, null);
    }

    public static RequestFuture createAsynFuture(int opaque, long timeoutMills, boolean reSendRequest, SemaphoreReleaseOnlyOnce once, Channel channel) {
        return new RequestFuture(opaque, timeoutMills, reSendRequest, once, channel, null);
    }

    public static RequestFuture createAsynFuture(int opaque, long timeoutMills, boolean reSendRequest, SemaphoreReleaseOnlyOnce once, Channel channel, CommandCallBack callBack) {
        return new RequestFuture(opaque, timeoutMills, reSendRequest, once, channel, callBack);
    }

    public void putRequest(RemotingCommand command) {
        this.requestCommand = command;
    }

    public void putResponse(RemotingCommand command) {
        this.responseCommand = command;
        this.latch.countDown();
    }

    public void release() {
        this.once.release();
    }

    public RemotingCommand waitResponse(long timeoutMills) throws Exception{
        this.latch.await(timeoutMills, TimeUnit.MILLISECONDS);
        return this.responseCommand;
    }

    public void executeCallBack() {
        if(this.callBack != null) {
            this.callBack.executeCallBack(this);
        }
    }

    public void setSendRequestOK(boolean sendRequestOK) {
        this.sendRequestOK = sendRequestOK;
    }

    public boolean isSendRequestOK() {
        return sendRequestOK;
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() - beginTimestamp > timeoutMills;
    }

    public RemotingCommand getResponseCommand() {
        return responseCommand;
    }

    public RemotingCommand getRequestCommand() {
        return requestCommand;
    }

    public boolean isReSendRequest() {
        return reSendRequest;
    }

    public Channel getChannel() {
        return channel;
    }

    public long getTimeoutMills() {
        return timeoutMills;
    }

    public CommandCallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CommandCallBack callBack) {
        this.callBack = callBack;
    }
}
