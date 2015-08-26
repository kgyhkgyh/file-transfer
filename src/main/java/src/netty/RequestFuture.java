package src.netty;

import src.RemotingCommand;
import src.util.SemaphoreReleaseOnlyOnce;

/**
 * Created by Administrator on 2015/8/26.
 */
public class RequestFuture {

    private volatile RemotingCommand requestCommand;

    private volatile boolean sendRequestOK = true;

    private final int opaque;

    private final long timeoutMills;

    private long beginTimestamp = System.currentTimeMillis();

    private final boolean reSendRequest;

//    private final SemaphoreReleaseOnlyOnce once;

    public RequestFuture(int opaque, long timeoutMills, boolean reSendRequest) {
        this.opaque = opaque;
        this.timeoutMills = timeoutMills;
        this.reSendRequest = reSendRequest;
//        this.once = once;
        this.sendRequestOK = sendRequestOK;
    }

    public void putRequest(final RemotingCommand command) {
        this.requestCommand = command;
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() - beginTimestamp > timeoutMills;
    }


}
