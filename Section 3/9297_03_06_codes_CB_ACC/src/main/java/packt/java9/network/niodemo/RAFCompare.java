package packt.java9.network.niodemo;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RAFCompare {
    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();
        RandomAccessFile raf1 = new RandomAccessFile("sample.txt", "r");
        RandomAccessFile raf2 = new RandomAccessFile("sample-copy.txt", "r");
        int b1 = 0, b2 = 0, pos = 1;
        while (b1 != -1 && b2 != -1) {
            if (b1 != b2) {
                System.out.println("Files differ at position " + pos);
            }
            pos++;
            b1 = raf1.read();
            b2 = raf2.read();
        }
        if (b1 != b2) {
            System.out.println("Files have different length");
        } else {
            System.out.println("Files are identical, you can delete one of them.");
        }
        raf1.close();
        raf2.close();
        long end = System.nanoTime();
        System.out.print("Execution time: " + (end - start) / 1000000 + "ms");
    }
}
