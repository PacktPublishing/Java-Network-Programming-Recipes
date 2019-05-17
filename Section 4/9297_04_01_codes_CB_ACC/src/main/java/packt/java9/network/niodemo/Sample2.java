package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Sample2 {

    public static void main(String[] args) throws IOException {
        RandomAccessFile sampleFile = new RandomAccessFile("sample2.txt", "rw");
        FileChannel channel = sampleFile.getChannel();
        ByteBuffer buf = ByteBuffer.allocate(11);
        final String sample = "Here we go.";
        byte[] sampleBytes = sample.getBytes();
        for( byte b : sampleBytes){
            buf.put(b);
        }
        buf.flip();
        channel.write(buf);
        sampleFile.close();
    }
}
