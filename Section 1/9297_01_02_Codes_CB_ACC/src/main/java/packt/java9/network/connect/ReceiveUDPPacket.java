package packt.java9.network.connect;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveUDPPacket {
    private static final int BUFLEN = 2000;

    public static void main(String[] args) throws IOException {

        DatagramSocket serverSocket = new DatagramSocket(1024);
        byte[] receiveData = new byte[BUFLEN];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        String sentence = new String(receivePacket.getData());
        System.out.println("RECEIVED: '" + sentence + "'");
    }
}
