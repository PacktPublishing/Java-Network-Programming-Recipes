package packt.java9.network.Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static java.lang.System.Logger.Level.INFO;

public class Server {
    private static final System.Logger LOG = System.getLogger(Server.class.getName());
    private final Map<Channel, Map<Integer, Consumer<Channel>>> performMap;
    private final Selector selector;
    private InetSocketAddress port = new InetSocketAddress(80);
    private volatile boolean running;
    private int timeout = 5000;
    private Consumer<Channel> acceptHandler;

    public Server() throws IOException {
        selector = Selector.open();
        performMap = new HashMap<>();
    }

    private static void info(String s) {
        LOG.log(INFO, s);
    }

    public Server withPort(int port) {
        this.port = new InetSocketAddress(port);
        return this;
    }

    public Server withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public void register(
            SelectableChannel channel,
            int op,
            Map<Integer, Consumer<Channel>> mapFunction
    ) throws ClosedChannelException {
        channel.register(selector, op);
        performMap.put(channel, mapFunction);
    }

    public void stop() {
        info("registering request to stop");
        running = false;
    }

    public Server withAcceptHandler(Consumer<Channel> acceptHandler) {
        this.acceptHandler = acceptHandler;
        return this;
    }

    public void start() throws IOException {
        running = true;
        try (final ServerSocketChannel ignored = startListening(acceptHandler)) {
            while (true) {
                final int nrOfChannels = selector.select(timeout);
                if (!running) {
                    info("not running any more");
                    return;
                }
                if (nrOfChannels == 0) {
                    info("waiting ...");
                } else {
                    info("handling requests");
                    handleReadyChannels();
                }
            }
        }
    }

    private ServerSocketChannel startListening(Consumer<Channel> acceptHandler) throws IOException {
        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(port);
        channel.configureBlocking(false);
        register(channel, SelectionKey.OP_ACCEPT, Map.of(SelectionKey.OP_ACCEPT, acceptHandler));
        return channel;
    }


    private void handleReadyChannels() throws IOException {
        Set<SelectionKey> keys = selector.selectedKeys();
        for (final SelectionKey key : keys) {
            final Channel channel = key.channel();
            Map<Integer, Consumer<Channel>> performer = performMap.get(channel);
            if (performer != null) {
                for (Integer action : performer.keySet()) {
                    if ((key.readyOps() & action) != 0) {
                        try {
                            performer.get(action).accept(channel);
                        } catch (RuntimeException ignored) {
                            channel.close();
                            break;
                        }
                    }
                }
            } else {
                info("There is nothig to do for channel " + channel);
            }
        }
        keys.removeAll(keys);
    }
}
