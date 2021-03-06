package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Sample1 {

    public static void main(String[] args) throws IOException {
        RandomAccessFile sampleFile = new RandomAccessFile("sample.txt", "rw");
        FileChannel channel = sampleFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(10);
        int nrBytes = channel.read(buf);
        while (nrBytes != -1) {
            System.out.println("Read " + nrBytes);
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.print((char) buf.get());
            }
            buf.clear();
            nrBytes = channel.read(buf);
        }
        sampleFile.close();
    }
}
