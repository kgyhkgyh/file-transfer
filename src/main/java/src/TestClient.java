package src;

import src.netty.NettyRemotingClient;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TestClient {

    public static void main(String[] args) {
        NettyRemotingClient client = new NettyRemotingClient();
        client.start();
        
        client.invokeSync("127.0.0.1:8000", new RemotingCommand(1,"send", "this is client"));
    }

}
