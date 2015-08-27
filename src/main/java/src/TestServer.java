package src;

import src.netty.NettyRemotingServer;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TestServer {

    public static void main(String[] args) {
        NettyRemotingServer server = new NettyRemotingServer(8000);
        server.start();
    }

}
