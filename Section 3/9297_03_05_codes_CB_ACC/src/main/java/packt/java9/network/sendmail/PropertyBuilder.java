package packt.java9.network.sendmail;

import java.util.Properties;

public class PropertyBuilder {

    public static Properties build(String ... s){
        Properties props = new Properties();
        for( int i = 0 ; i < s.length ; i+=2 ){
            props.put(s[i], s[i+1]);
        }
        return props;
    }
}
