package src.transport.protocal;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/30.
 */
public class FileSegmentRequest implements CommandBody, Serializable{

    private String fileName;

    private long position;

    private int blockSize;

    private byte[] content;

    public FileSegmentRequest() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
