package packt.java9.irc;

import packt.java9.config.Config;
import packt.java9.config.ConfigReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class PacktBot {


  public static void main(String[] args) throws Exception {

    final Config config = new ConfigReader().getConfig();
    String server = config.get("server");
    int port = Integer.valueOf(config.get("port"));
    String nick = config.get("nick");
    String login = config.get("login");
    String channel = config.get("channel");

    Socket socket = new Socket(server, port);
    final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("utf-8")));
    final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("utf-8")));

    try (Bot bot = new Bot(writer, reader)) {
      bot.initiateConnection(nick, login);
      if (!bot.connectionAccepted()) {
        return;
      }
      bot.joinChannel(channel);
      new CommandConnection(bot);
      bot.processLines(channel);
    }
  }


}
