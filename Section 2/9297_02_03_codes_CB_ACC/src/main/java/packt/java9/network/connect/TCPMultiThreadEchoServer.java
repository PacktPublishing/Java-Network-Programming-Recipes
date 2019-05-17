package packt.java9.network.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPMultiThreadEchoServer {
    private static final int TELNET_PORT = 8080;


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(TELNET_PORT);
        while( true ) {
            Socket clientSocket = serverSocket.accept();
            InetSocketAddress remote = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
            System.out.println("connection from port=" + remote.getPort() + " host=" + remote.getHostName());
            new Thread(
                    () -> {
                        InputStream in = null;
                        try {
                            in = clientSocket.getInputStream();
                            OutputStream out = clientSocket.getOutputStream();
                            while (clientSocket.isConnected()
                                && in.transferTo(out) > 0 );
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            ).start();
        }

    }
}
