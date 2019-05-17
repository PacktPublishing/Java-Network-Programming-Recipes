package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class FileReader {

    public static void main(String[] args) throws IOException {
        final RandomAccessFile sampleFile = new RandomAccessFile("sample.txt", "rw");
        final FileChannel channel = sampleFile.getChannel();
        final ByteBuffer buf = ByteBuffer.allocate(4098);
        int nrBytes = channel.read(buf);
        while (nrBytes != -1) {
            System.out.println("Read " + nrBytes);
            buf.flip();
            while (buf.hasRemaining()) {
                buf.get();
                //System.out.print((char) buf.get());
            }
            buf.clear();
            nrBytes = channel.read(buf);
        }
        sampleFile.close();
    }
}
