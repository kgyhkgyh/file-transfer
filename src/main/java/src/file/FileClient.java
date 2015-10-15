package src.file;

import org.apache.commons.io.IOUtils;
import src.transport.CommandCallBack;
import src.transport.netty.NettyRemotingClient;
import src.transport.netty.RequestFuture;
import src.transport.protocal.FileSegmentRequest;
import src.transport.protocal.FileTaskRequest;
import src.transport.protocal.RemotingCommand;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2015/9/1.
 */
public class FileClient {

    private final NettyRemotingClient client;

    public FileClient() {
        client = new NettyRemotingClient();
    }

    public void start() {
        client.start();
    }

    public void sendFile(String filePath, String addr) {
        RemotingCommand command = new RemotingCommand(1, 1, false);
        FileTaskRequest request = new FileTaskRequest();
        final File file = new File(filePath);
        request.setFileName(file.getName());
        request.setBlockSize(100);
        request.setFileSize(file.length());
        command.setBody(request);
        client.invokeAsync(addr, command, 10 * 1000, requestFuture -> {
            boolean done = false;
            RandomAccessFile raf = null;
            FileChannel channel = null;
            try {
                int blockSize = 100;
                raf = new RandomAccessFile(file, "r");
                channel = raf.getChannel();
                long position = 0;
                ByteBuffer buffer = ByteBuffer.allocate(blockSize);
                int size = blockSize;
                while (!done) {
                    channel.position(position);
                    channel.read(buffer);
                    FileSegmentRequest segmentRequest = new FileSegmentRequest();
                    segmentRequest.setBlockSize(size);
                    segmentRequest.setFileName(file.getName());
                    segmentRequest.setPosition(position);
                    if (position +  blockSize >= file.length()) {
                        size = (int) (file.length() - position);
                        done = true;
                        byte[] b = new byte[size];
                        buffer.flip();
                        buffer.get(b, 0, size);
                        segmentRequest.setContent(b);
                    }else {
                        segmentRequest.setContent(buffer.array());
                    }
                    buffer.clear();

                    RemotingCommand command1 = new RemotingCommand(2, 1, false);
                    command1.setBody(segmentRequest);
                    client.invokeOneway(addr, command1, 100 * 1000);

                    position += size;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(channel);
                IOUtils.closeQuietly(raf);
            }
        });
    }
}
