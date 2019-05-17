package packt.java9.network.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import static java.lang.System.Logger.Level.INFO;

public class Client {
    private static final System.Logger LOG = System.getLogger(Client.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", RandomStreamServer.PORT_NUMBER));
            ByteBuffer buffer = ByteBuffer.allocate(RandomStreamServer.BUFFER_SIZE);
            int i = 1;
            for (; ; ) {
                socketChannel.read(buffer);
                buffer.flip();
                byte[] bytes = new byte[buffer.limit()];
                buffer.get(bytes);
                LOG.log(INFO, String.format("Iteration %d. %d bytes read", i++, buffer.limit()));
                buffer.clear();
                Thread.sleep(1000);
            }
        }
    }
}
