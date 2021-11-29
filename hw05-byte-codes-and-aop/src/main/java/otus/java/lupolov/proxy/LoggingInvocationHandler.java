package otus.java.lupolov.proxy;

import otus.java.lupolov.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class LoggingInvocationHandler implements InvocationHandler {

    public static final String LOG_STR_TEMPLATE = "Invoke method '%s' with param: %s%n";

    private final Object target;
    private final Method[] targetMethods;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
        this.targetMethods = target.getClass().getMethods();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Method targetMethod = Arrays.stream(targetMethods)
                .filter(m -> m.getName().equals(method.getName()))
                .filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No such method"));

        if (targetMethod.isAnnotationPresent(Log.class)) {
            System.out.printf(LOG_STR_TEMPLATE, method.getName(), Arrays.toString(args));
        }

        return method.invoke(target, args);
    }
}
