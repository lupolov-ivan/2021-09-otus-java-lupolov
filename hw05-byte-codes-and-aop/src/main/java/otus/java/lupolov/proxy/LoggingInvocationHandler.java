package otus.java.lupolov.proxy;

import otus.java.lupolov.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LoggingInvocationHandler implements InvocationHandler {

    public static final String LOG_STR_TEMPLATE = "Invoke method '%s' with param: %s%n";

    private final Object target;
    private final List<Method> targetMethods;

    public LoggingInvocationHandler(Object target) {
        this.target = target;
        this.targetMethods = Arrays.stream(target.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(Log.class))
                .toList();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Optional<Method> targetMethod = targetMethods.stream()
                .filter(m -> m.getName().equals(method.getName()))
                .filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
                .findFirst();

        if (targetMethod.isPresent()) {
            System.out.printf(LOG_STR_TEMPLATE, method.getName(), Arrays.toString(args));
        }

        return method.invoke(target, args);
    }
}
