package packt.java9.config;

import java.util.Collection;

public interface Config {

    Collection<String> getAllowedIps();
    Collection<String> getDeniedIps();

}
