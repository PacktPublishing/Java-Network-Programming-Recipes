package packt.java9.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class ConfigReader {

    final Collection<String> allowedDomains;
    final Collection<String> deniedDomains;

    public ConfigReader() {
        final Properties filterProperties = new Properties();
        try {
            filterProperties.load(getClass().getClassLoader().getResourceAsStream("filter.properties"));
        } catch (IOException ignored) {
        } finally {
            final String allowedDomainsCSV = filterProperties.getProperty("allowed");
            final String deniedDomainsCSV = filterProperties.getProperty("denied");
            allowedDomains = Arrays.asList(allowedDomainsCSV.split(","));
            deniedDomains = Arrays.asList(deniedDomainsCSV.split(","));
        }
    }

    public Config getConfig() {
        return new Config() {
            @Override
            public Collection<String> getAllowedDomains() {
                return allowedDomains;
            }

            @Override
            public Collection<String> getDeniedDomains() {
                return deniedDomains;
            }
        };
    }
}
