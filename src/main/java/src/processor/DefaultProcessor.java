package src.processor;

import io.netty.channel.ChannelHandlerContext;
import src.protocal.RemotingCommand;
import src.netty.NettyRequestProcessor;

/**
 * Created by a on 2015/8/27.
 */
public class DefaultProcessor implements NettyRequestProcessor {

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        String content = request.getContent();
        System.out.println("服务端接受的消息是：" + content);
        RemotingCommand remotingCommand = new RemotingCommand(1, 2, true);
        remotingCommand.setContent("服务端返回的消息：" + content);
        return remotingCommand;
    }

}
