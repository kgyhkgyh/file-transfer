package src;

import src.netty.NettyRemotingClient;
import src.protocal.RemotingCommand;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TestClient {

    public static void main(String[] args) {
        NettyRemotingClient client = new NettyRemotingClient();
        client.start();

        System.out.println("开始时间"+System.currentTimeMillis());
        for (int i = 0; i < 10000; i++) {
            RemotingCommand command = new RemotingCommand(1, 1, false);
            command.setContent("我要发送消息--" + i);
//            client.invokeAsync("127.0.0.1:8000", command, 1000);
            client.invokeSync("127.0.0.1:8000", command, 1000);
        }
        System.out.println("结束时间"+System.currentTimeMillis());
    }

}
