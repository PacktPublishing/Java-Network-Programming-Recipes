package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class Client {

    public static void main(String[] args) throws IOException {

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.connect(new InetSocketAddress("localhost", Server.PORT));
            RandomAccessFile input = new RandomAccessFile("sample.txt", "r");
            FileChannel fileChannel = input.getChannel();
            long transferred = 0;
            while (transferred < fileChannel.size()) {
                transferred += fileChannel.transferTo(transferred, fileChannel.size()-transferred, socketChannel);
            }
            socketChannel.close();
            fileChannel.close();
            input.close();
        }
    }
}
