package packt.java9.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class ConfigReader {

    final Collection<String> allowedIps;
    final Collection<String> deniedIps;

    public ConfigReader() {
        final Properties filterProperties = new Properties();
        try {
            filterProperties.load(getClass().getClassLoader().getResourceAsStream("filter.properties"));
        } catch (IOException ignored) {
        } finally {
            final String allowedIpsCSV = filterProperties.getProperty("allowed");
            final String deniedIpsCSV = filterProperties.getProperty("denied");
            allowedIps = Arrays.asList(allowedIpsCSV.split(","));
            deniedIps = Arrays.asList(deniedIpsCSV.split(","));
        }
    }

    public Config getConfig() {
        return new Config() {
            @Override
            public Collection<String> getAllowedIps() {
                return allowedIps;
            }

            @Override
            public Collection<String> getDeniedIps() {
                return deniedIps;
            }
        };
    }
}
