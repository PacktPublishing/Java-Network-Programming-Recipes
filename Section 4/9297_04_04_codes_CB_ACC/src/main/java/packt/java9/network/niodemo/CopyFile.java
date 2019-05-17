package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class CopyFile {

    public static void main(String[] args) throws IOException {
        try (RandomAccessFile input = new RandomAccessFile("sample.txt", "r");
             FileChannel from = input.getChannel();
             RandomAccessFile output = new RandomAccessFile("sample-copy.txt", "rw");
             FileChannel to = output.getChannel()
        ) {
            to.transferFrom(from, 0, from.size());
           // from.transferTo(0,from.size(),to);
        }
    }
}
