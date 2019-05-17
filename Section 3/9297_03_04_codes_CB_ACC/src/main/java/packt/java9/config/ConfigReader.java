package packt.java9.config;

import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

  final Properties configProperties;

  public ConfigReader() {
    configProperties = new Properties();
    try {
      configProperties.load(getClass().getClassLoader().getResourceAsStream("ircbot.properties"));
    } catch (IOException e) {
      throw new RuntimeException("Configuration file can not be read", e);
    }
  }

  public Config getConfig() {
    return new Config() {
      @Override
      public String get(String key) {
        return configProperties.getProperty(key);
      }
    };
  }
}
