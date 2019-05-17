package packt.java9.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandConnection {
  private static final int TELNET_PORT = 8080;
  private final Bot bot;

  public CommandConnection(Bot bot) {
    this.bot = bot;
    final ExecutorService listenerService = Executors.newFixedThreadPool(1);
    listenerService.submit(() -> listener());
  }


  public void listener() {
    final ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(TELNET_PORT);
      final ExecutorService service = Executors.newFixedThreadPool(1);
      while (true) {
        final Socket clientSocket = serverSocket.accept();
        final InetSocketAddress remote = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        System.out.println("connection from port=" + remote.getPort() + " host=" + remote.getHostName());
        service.submit(
            () -> {
              try (final InputStream in = clientSocket.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                while (clientSocket.isConnected()) {
                  String line = reader.readLine();
                  bot.sendLine(line);
                }
                System.out.println("disconnection from port=" + remote.getPort() +
                    " host=" + remote.getHostName());
              } catch (IOException e) {
                e.printStackTrace();
              }
            }
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
