package src.transport;

import src.transport.netty.RequestFuture;

/**
 * Created by Administrator on 2015/9/1.
 */
public interface CommandCallBack {

    void executeCallBack(final RequestFuture requestFuture);

}
