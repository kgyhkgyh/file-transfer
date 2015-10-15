package src;

import src.file.FileClient;
import src.transport.CommandCallBack;
import src.transport.netty.NettyRemotingClient;
import src.transport.netty.RequestFuture;
import src.transport.protocal.FileSegmentRequest;
import src.transport.protocal.FileTaskRequest;
import src.transport.protocal.RemotingCommand;

import java.io.File;
import java.io.RandomAccessFile;

/**
 * Created by Administrator on 2015/8/25.
 */
public class TestClient {

    public static void main(String[] args) {
        FileClient client = new FileClient();
        client.start();
        client.sendFile("D:\\学习资料\\abc.txt", "127.0.0.1:8000");
    }

}
