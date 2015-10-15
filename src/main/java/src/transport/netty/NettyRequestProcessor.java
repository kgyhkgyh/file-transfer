package src.transport.netty;

import io.netty.channel.ChannelHandlerContext;
import src.transport.protocal.RemotingCommand;

/**
 * Created by Administrator on 2015/8/26.
 */
public interface NettyRequestProcessor {

    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)
            throws Exception;
}
