package packt.java9.network.connect;

import packt.java9.config.Config;
import packt.java9.config.ConfigReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPMultiThreadEchoServer {
    private static final int TELNET_PORT = 8080;

    private static Config config = new ConfigReader().getConfig();

    public static void main(String[] args) throws IOException {
        final ServerSocket serverSocket = new ServerSocket(TELNET_PORT);
        final ExecutorService service = Executors.newFixedThreadPool(3);
        while (true) {
            final Socket clientSocket = serverSocket.accept();
            final InetSocketAddress remote = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
            final InetAddress address = remote.getAddress();
            final String hostAddress = address.getHostAddress();
            System.out.println("connection from port=" + remote.getPort()
                    + " ip=" + hostAddress);
            if (isAllowed(hostAddress)) {
                serve(service, clientSocket);
            } else {
                clientSocket.close();
                System.out.println("connection from port=" + remote.getPort()
                        + " ip=" + hostAddress + " is refused");
            }
        }
    }

    private static boolean isAllowed(String hostAddress) {
        return config.getAllowedIps().stream().anyMatch(hostAddress::matches)
                &&
                config.getDeniedIps().stream().noneMatch(hostAddress::matches);
    }

    private static void serve(ExecutorService service, Socket clientSocket) {
        service.submit(
                () -> {
                    try (final InputStream in = clientSocket.getInputStream();
                         final OutputStream out = clientSocket.getOutputStream()) {
                        while (clientSocket.isConnected() && in.transferTo(out) > 0) ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
