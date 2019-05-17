package packt.java9.irc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;

public class Bot implements Closeable{
  final BufferedWriter writer;
  final BufferedReader reader;

  public Bot(BufferedWriter writer, BufferedReader reader) {
    this.writer = writer;
    this.reader = reader;
  }

  public void processLines(String channel) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      if (line.toUpperCase().startsWith("PING ")) {
        // We must respond to PINGs to avoid being disconnected.
        writer.write("PONG " + line.substring(5) + "\r\n");
        writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
        writer.flush();
        echo("ping line: "+line);
      } else {
        echo(line);
      }
    }
  }

  public void joinChannel(String channel) throws IOException {
    writer.write("JOIN " + channel + "\r\n");
    writer.flush();
  }

  public boolean connectionAccepted() throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      echo(line);
      if (line.indexOf("004") >= 0) {
        echo("We are logged in");
        return true;
      } else if (line.indexOf("433") >= 0) {
        echo("Nickname is already in use.");
        return false;
      }
    }
    return false;
  }

  public void initiateConnection(String nick, String login) throws IOException {
    writer.write("NICK " + nick + "\r\n");
    writer.write("USER " + login + " 0 * :Packt Demo\r\n");
    writer.flush();
  }

  public void sendLine(String line) throws IOException {
    writer.write(line+"\r\n");
    writer.flush();
  }

  private void echo(String line) {
    System.out.println(line);
  }

  @Override
  public void close() throws IOException {
    writer.close();
    reader.close();
  }
}
