package socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(8000));
			System.out.println("接受者建立完成");
			while(true) {
				Socket socket = serverSocket.accept();
				Reader reader = new InputStreamReader(socket.getInputStream());
				char chars[] = new char[64];
				int len;
				StringBuilder sb = new StringBuilder();
				while ((len = reader.read(chars)) != -1) {
					sb.append(new String(chars, 0, len));
				}
				System.out.println("from client: " + sb);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
