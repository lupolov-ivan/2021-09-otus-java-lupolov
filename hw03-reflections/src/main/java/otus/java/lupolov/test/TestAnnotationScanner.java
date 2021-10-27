package otus.java.lupolov.test;

import otus.java.lupolov.test.annotation.After;
import otus.java.lupolov.test.annotation.Before;
import otus.java.lupolov.test.annotation.Test;
import otus.java.lupolov.test.data.TestClassData;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestAnnotationScanner {

    public <T>TestClassData<T> scanClass(Class<T> clazz) {

        var classData = new TestClassData<T>();

        Constructor<T> constructor = getConstructor(clazz);
        classData.setConstructor(constructor);

        Method[] methods = clazz.getMethods();

        var isBeforeMethodAlreadySet = false;
        var isAfterMethodAlreadySet = false;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                classData.getTestMethods().add(method);
            }

            if (method.isAnnotationPresent(Before.class)) {
                if(isBeforeMethodAlreadySet) {
                    throw new IllegalStateException("Test class must contain only one method marked annotation '@Before'");
                }
                classData.setBeforeMethod(method);
                isBeforeMethodAlreadySet = true;
            }

            if (method.isAnnotationPresent(After.class)) {
                if(isAfterMethodAlreadySet) {
                    throw new IllegalStateException("Test class must contain only one method marked annotation '@After'");
                }
                classData.setAfterMethod(method);
                isAfterMethodAlreadySet = true;
            }
        }

        return classData;
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Test class must have zero-args constructor", e);
        }
    }
}
