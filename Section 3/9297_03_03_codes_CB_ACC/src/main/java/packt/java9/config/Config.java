package packt.java9.config;

import java.util.Collection;

public interface Config {

    Collection<String> getAllowedDomains();
    Collection<String> getDeniedDomains();

}
