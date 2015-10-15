package src.file;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2015/8/30.
 */
public class FileStorage {

    private FileChannel fileChannel;

    private int blockSize;

    private long fileSize;

    private AtomicLong position;

    private String md5;

    private int totalCount;

    private long lastModifyTimestamp;

    private BitSet segments;

    private static final int TIMEOUT_MILLS = 60 * 1000;

    public FileStorage(FileChannel fileChannel, long fileSize, String md5, int blockSize) {
        this.fileChannel = fileChannel;
        this.fileSize = fileSize;
        this.position = new AtomicLong(0);
        this.md5 = md5;
        this.blockSize = blockSize;
        this.lastModifyTimestamp = System.currentTimeMillis();
        this.totalCount = (int) (fileSize / blockSize) + (fileSize % blockSize == 0 ? 0 : 1);
        this.segments = new BitSet(totalCount);
    }

    public boolean isFinished() {
        synchronized (segments) {
            return segments.cardinality() == totalCount;
        }
    }

    public void close() {
        IOUtils.closeQuietly(this.fileChannel);
    }

    public void write(long position, byte[] content) {
        ByteBuffer buffer = ByteBuffer.allocate(blockSize);
        buffer.put(content);
        buffer.flip();
        try {
            fileChannel.position(position);
            fileChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (segments) {
            segments.set((int) (position / blockSize));
        }
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() - lastModifyTimestamp > TIMEOUT_MILLS;
    }

}
