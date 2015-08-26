package nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioBuffer {
	public static void main(String[] args) {
		try {
			RandomAccessFile raf = new RandomAccessFile("d:/test.txt","rw");
			FileChannel channel = raf.getChannel();
			ByteBuffer bb = ByteBuffer.allocate(50);
			int read = channel.read(bb);
			while(read != -1) {
				bb.flip();
				while(bb.hasRemaining()) {
					System.out.println((char)bb.get());
				}
				bb.clear();
				read = channel.read(bb);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
