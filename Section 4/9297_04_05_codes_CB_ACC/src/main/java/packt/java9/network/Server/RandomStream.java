package packt.java9.network.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Random;

import static java.lang.System.Logger.Level.INFO;

public class RandomStream {
    public static final int PORT_NUMBER = 1027;
    public static final int BUFFER_SIZE = 1024 * 1024;
    private static final System.Logger LOG = System.getLogger(RandomStream.class.getName());
    final Random rnd = new Random();
    final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    final byte[] b = new byte[BUFFER_SIZE];
    private Server server;

    public static void main(String[] args) throws IOException {
        new RandomStream().start();
    }

    private void start() throws IOException {
        new Thread(() -> {
            try {
                for (int i = 20; i > 0; i--) {
                    Thread.sleep(1000);
                    LOG.log(INFO, "" + i + "sec till stop");
                }
            } catch (InterruptedException ignored) {
            }
            server.stop();
        }).start();
        server = new Server().withPort(PORT_NUMBER).withTimeout(5000).withAcceptHandler(this::acceptConnection);
        server.start();
    }

    private void writeRandomBytes(Channel channel) {
        try {
            SocketChannel socketChannel = (SocketChannel) channel;
            LOG.log(INFO, "Sending random bytes to channel " + socketChannel);
            rnd.nextBytes(b);
            buffer.put(b);
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptConnection(Channel channel) {
        try {
            LOG.log(INFO, "Accepting connection ... ");
            final ServerSocketChannel socket = (ServerSocketChannel) channel;
            final SocketChannel socketChannel = socket.accept();
            socketChannel.configureBlocking(false);
            server.register(socketChannel, SelectionKey.OP_WRITE, Map.of(SelectionKey.OP_WRITE, this::writeRandomBytes));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
