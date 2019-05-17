package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;

class Converter {
    private static final byte[] poison = {0};

    public static void main(String[] args) throws IOException, InterruptedException {
        final BlockingQueue<byte[]> queue = new LinkedBlockingDeque<>();
        final RandomAccessFile sampleInput = new RandomAccessFile("sample.txt", "rw");
        final RandomAccessFile sampleOutput = new RandomAccessFile("sampleUC.txt", "rw");
        final FileChannel input = sampleInput.getChannel();
        final FileChannel output = sampleOutput.getChannel();
        ByteBuffer label = ByteBuffer.allocate(5);
        ByteBuffer continuation = ByteBuffer.allocate(1);
        ByteBuffer codeBuf = ByteBuffer.allocate(66);
        ByteBuffer sorter = ByteBuffer.allocate(8);
        ByteBuffer newLine = ByteBuffer.allocate(1);
        ByteBuffer[] line = {label, continuation, codeBuf, sorter, newLine};
        long nrBytes = input.read(line);
        startNumberCheckingThread(queue);
        Consumer<Consumer<ByteBuffer>> allBuffers = c -> {
            for (final ByteBuffer buf : line) {
                c.accept(buf);
            }
        };
        while (nrBytes != -1) {
            allBuffers.accept(ByteBuffer::flip);
            byte[] sorterBytes = new byte[8];
            sorter.get(sorterBytes);
            queue.put(sorterBytes);

            byte[] fortran = new byte[66];
            codeBuf.get(fortran);
            for (int i = 0; i < fortran.length; i++) {
                if (fortran[i] >= 0x61 && fortran[i] <= 0x7a) {
                    fortran[i] &= ~0x20;
                }
            }
            codeBuf.clear();
            codeBuf.put(fortran);
            allBuffers.accept(ByteBuffer::rewind);
            output.write(line);
            allBuffers.accept(ByteBuffer::clear);
            nrBytes = input.read(line);
        }
        queue.put(poison);
        sampleInput.close();
    }

    private static void startNumberCheckingThread(BlockingQueue<byte[]> queue) {
        new Thread(() -> {
            byte[] last = null;
            while (true) {
                try {
                    byte[] next = queue.take();
                    if (next[0] == 0) return;
                    last = checkNumbersIncrease(last, next);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private static byte[] checkNumbersIncrease(byte[] last, byte[] next) {
        if (last != null) {
            String sooner = new String(last), later = new String(next);
            if (Integer.valueOf(sooner.trim()) >= Integer.valueOf(later.trim())) {
                System.err.println(String.format("%s comes sooner than %s and still the 2nd is not larger", sooner, later));
            }
        }
        last = next;
        return last;
    }
}
