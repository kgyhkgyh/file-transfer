package io;

import java.io.*;
import java.nio.CharBuffer;
import java.util.stream.Stream;

/**
 * Created by Administrator on 2015/7/14.
 */
public class FileReaderTest {

    public static void main(String[] args) throws IOException {
        writerFile();
        readFile();
    }

    private static void writerFile() throws IOException {
        FileWriter fileWriter = null;
        try {
            File file = new File("d:/test.txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            char[] c = "abc".toCharArray();
            fileWriter.write(c);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileWriter.close();
        }
    }

    private static void readFile() throws IOException {
        FileReader fileReader = null;
        File file = new File("d:/test.txt");
        fileReader = new FileReader(file);
        CharBuffer charBuffer = CharBuffer.allocate(10);
        fileReader.read(charBuffer);
        System.out.println(charBuffer.array());
    }

}
