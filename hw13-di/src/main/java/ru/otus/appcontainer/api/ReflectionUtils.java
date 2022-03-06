package ru.otus.appcontainer.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private ReflectionUtils() {}

    public static Object getConfigInstance(Class<?> clazz) {
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            return declaredConstructor.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            String errMsg = String.format("Error during creation config instance [%s]", clazz.getName());
            throw new ComponentInitializationException(errMsg);
        }
    }

    public static Object getComponentInstance(Object invocationTarget, Method method, Object... args) {
        try {
            return method.invoke(invocationTarget, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            String errMsg = String.format("Error during component creation [%s]", method.getName());
            throw new ComponentInitializationException(errMsg);
        }
    }
}