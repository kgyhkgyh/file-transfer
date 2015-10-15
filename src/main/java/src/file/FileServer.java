package src.file;

import src.transport.netty.NettyRemotingServer;

/**
 * Created by Administrator on 2015/9/1.
 */
public class FileServer {

    private final NettyRemotingServer server;

    public FileServer(int port, String targetDir) {
        server = new NettyRemotingServer(port, targetDir);
    }

    public void start() {
        server.start();
    }
}
