package io;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Administrator on 2015/7/14.
 */
public class Md5Test {

    public static void main(String[] args) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File("d:/test.txt"));
            String md5 = DigestUtils.md5Hex(fileInputStream);
            System.out.println(md5);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }

}
