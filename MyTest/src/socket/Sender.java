package socket;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

public class Sender {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 8000);
			Writer writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write("Hello Server.");
			System.out.println(System.in);
			writer.flush();// 写完后要记得flush
			writer.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
