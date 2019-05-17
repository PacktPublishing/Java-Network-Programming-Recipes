import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflThrow {
    private class Nested {
        private void m(){
            System.out.println("m called");
        }
    }
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ReflThrow me = new ReflThrow();
        Nested n = me.new Nested();
        n.m();
        Method m = Nested.class.getDeclaredMethod("m");
        m.invoke(n);
    }
}
