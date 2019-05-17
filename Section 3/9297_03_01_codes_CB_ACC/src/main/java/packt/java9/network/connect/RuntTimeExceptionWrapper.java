package packt.java9.network.connect;

import java.util.function.Function;

public class RuntTimeExceptionWrapper {

    public static <T, R> Function<T, R> noException(ExceptionalFunction<T, R> f) {
        return (T r) -> {
            try {
                return f.apply(r);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }


    public interface ExceptionalFunction<T, R> {
        R apply(T r) throws Exception;
    }
}
