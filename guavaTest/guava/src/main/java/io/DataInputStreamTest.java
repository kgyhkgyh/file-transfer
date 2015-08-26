package io;

import java.io.*;

/**
 * Created by Administrator on 2015/7/14.
 */
public class DataInputStreamTest {

    public static void main(String[] args) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File("d:/test.txt"));
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeChar(87);
            dos.writeUTF("abc");
            fis = new FileInputStream(new File("d:/test.txt"));
            DataInputStream dis = new DataInputStream(fis);
            System.out.println(dis.readChar());
            System.out.println(dis.readUTF());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
