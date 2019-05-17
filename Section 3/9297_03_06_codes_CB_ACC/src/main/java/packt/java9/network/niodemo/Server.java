package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    public static final int PORT = 1027;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        RandomAccessFile output = new RandomAccessFile("sample-copy.txt", "rw");
        FileChannel fileChannel = output.getChannel();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        SocketChannel socketChannel = serverSocketChannel.accept();

        long transferred = 0;
        long chunk;
        do {
            chunk = fileChannel.transferFrom(socketChannel, transferred, Long.MAX_VALUE);
            transferred += chunk;
        } while (chunk > 0);

        socketChannel.close();
        fileChannel.close();
        output.close();
    }
}
