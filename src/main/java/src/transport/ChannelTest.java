package src.transport;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2015/9/1.
 */
public class ChannelTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile rw = new RandomAccessFile("D:\\target/123.txt", "rw");
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("asdfasdfsdfsdfsdf".getBytes());

        FileChannel channel = rw.getChannel();
        channel.position(0);
        channel.write(buf);

        channel.close();
        rw.close();
    }

}
