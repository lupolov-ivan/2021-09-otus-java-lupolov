package otus.java.lupolov.test.data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestClassData<T> {

    private Method beforeMethod;
    private List<Method> testMethods = new ArrayList<>();
    private Method afterMethod;
    private Constructor<T> constructor;

    public Method getBeforeMethod() {
        return beforeMethod;
    }

    public void setBeforeMethod(Method beforeMethod) {
        this.beforeMethod = beforeMethod;
    }

    public List<Method> getTestMethods() {
        return testMethods;
    }

    public void setTestMethods(List<Method> testMethods) {
        this.testMethods = testMethods;
    }

    public Method getAfterMethod() {
        return afterMethod;
    }

    public void setAfterMethod(Method afterMethod) {
        this.afterMethod = afterMethod;
    }

    public Constructor<T> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<T> constructor) {
        this.constructor = constructor;
    }
}
