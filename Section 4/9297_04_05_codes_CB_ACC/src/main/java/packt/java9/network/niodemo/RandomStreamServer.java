package packt.java9.network.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.Set;

import static java.lang.System.Logger.Level.INFO;

public class RandomStreamServer {
    public static final int PORT_NUMBER = 1027;
    public static final int BUFFER_SIZE = 1024 * 1024;
    private static final InetSocketAddress PORT = new InetSocketAddress(PORT_NUMBER);
    private static final System.Logger LOG = System.getLogger(RandomStreamServer.class.getName());
    final Random rnd = new Random();
    final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    final byte[] b = new byte[BUFFER_SIZE];
    Selector selector;

    public static void main(String[] args) throws IOException {
        new RandomStreamServer().serve();
    }

    void serve() throws IOException {
        initializeSelector();
        startListening();
        while (true) {
            if (selector.select(1000) == 0) {
                LOG.log(INFO, "waiting ...");
            } else {
                handleChannelsReady();
            }
        }
    }

    private void startListening() throws IOException {
        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(PORT);
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void initializeSelector() throws IOException {
        selector = Selector.open();
    }

    private void handleChannelsReady() throws IOException {
        Set<SelectionKey> keys = selector.selectedKeys();
        for (final SelectionKey key : keys) {
            if (key.isAcceptable()) {
                acceptConnection(key);
            }
            if (key.isWritable()) {
                final SocketChannel socketChannel = (SocketChannel) key.channel();
                try {
                    writeRandomBytes(socketChannel);
                } catch (IOException e) {
                    socketChannel.close();
                    break;
                }
            }
        }
        keys.removeAll(keys);
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        LOG.log(INFO, "Accepting connection ... ");
        final ServerSocketChannel socket = (ServerSocketChannel) key.channel();
        final SocketChannel socketChannel = socket.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    private void writeRandomBytes(SocketChannel socketChannel) throws IOException {
        LOG.log(INFO, "Sending random bytes to channel " + socketChannel);
        rnd.nextBytes(b);
        buffer.put(b);
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
    }
}
