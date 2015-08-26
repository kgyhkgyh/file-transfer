package nio;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SelectorTest {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("127.0.0.1", 8000);
		SocketChannel channel = socket.getChannel();
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
	}
}
