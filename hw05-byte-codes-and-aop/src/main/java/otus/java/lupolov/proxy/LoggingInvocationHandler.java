package otus.java.lupolov.proxy;

import otus.java.lupolov.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

public class LoggingInvocationHandler implements InvocationHandler {

    public static final String LOG_STR_TEMPLATE = "Invoke method '%s' with param: %s%n";

    private final Object target;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> targetClass = target.getClass();
        Method targetMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());

        if (targetMethod.isAnnotationPresent(Log.class)) {
            System.out.printf(LOG_STR_TEMPLATE, method.getName(), Arrays.toString(args));
        }

        return method.invoke(target, args);
    }
}
