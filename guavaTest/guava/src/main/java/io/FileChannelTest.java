package io;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Administrator on 2015/7/14.
 */
public class FileChannelTest {

    public static void main(String[] args) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            FileChannel fileInChannel = new FileInputStream(new File("d:/test.txt")).getChannel();
            FileChannel fileOutChannel = new FileOutputStream(new File("d:/test2.txt")).getChannel();
            fileOutChannel.transferFrom(fileInChannel, 0 , fileInChannel.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

}
