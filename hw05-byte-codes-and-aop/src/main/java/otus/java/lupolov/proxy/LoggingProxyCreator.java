package otus.java.lupolov.proxy;

import java.lang.reflect.Proxy;

public class LoggingProxyCreator {

    private LoggingProxyCreator() {}

    public static <T> T getProxyInstance(Class<T> targetInterface, Object targetInstance) {

        var invocationHandler = new LoggingInvocationHandler(targetInstance);
        var interfaces = new Class<?>[]{targetInterface};
        Object proxyInstance = Proxy.newProxyInstance(targetInterface.getClassLoader(), interfaces, invocationHandler);

        return targetInterface.cast(proxyInstance);
    }
}
