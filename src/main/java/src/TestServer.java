package src;

import src.file.FileServer;
import src.transport.netty.NettyRemotingServer;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TestServer {

    public static void main(String[] args) {
        FileServer server = new FileServer(8000, "d:/target");
        server.start();
    }

}
