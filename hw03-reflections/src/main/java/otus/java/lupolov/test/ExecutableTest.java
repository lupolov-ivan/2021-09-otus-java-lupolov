package otus.java.lupolov.test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;

public class ExecutableTest<T> {

    private final Constructor<T> testClassConstructor;
    private final Method methodBefore;
    private final Method testMethod;
    private final Method methodAfter;

    public ExecutableTest(Constructor<T> testClassConstructor, Method methodBefore, Method testMethod, Method methodAfter) {
        this.testClassConstructor = testClassConstructor;
        this.methodBefore = methodBefore;
        this.testMethod = testMethod;
        this.methodAfter = methodAfter;
    }

    public boolean execute() {

        T instance = getInstanceTestClass();

        Optional.ofNullable(methodBefore)
                .ifPresent(method -> callBeforeAfterMethod(methodBefore, instance));

        boolean testExecutionResult = callTestMethod(instance);

        Optional.ofNullable(methodAfter)
                .ifPresent(method -> callBeforeAfterMethod(methodAfter, instance));

        return testExecutionResult;
    }

    public T getInstanceTestClass() {
        try {
            return this.testClassConstructor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed initialization test class constructor", e);
        }
    }

    public boolean callTestMethod(T instance) {
        try {
            testMethod.invoke(instance);
            return true;
        } catch (Exception e) {
            System.err.printf("Test '%s' FAILED.%n", testMethod.getName());
            e.printStackTrace();
            return false;
        }
    }

    public void callBeforeAfterMethod(Method method, T instance) {
        try {
            method.invoke(instance);
        } catch (Exception e) {
            throw new IllegalStateException(method.getName() +" invocation failed", e);
        }
    }
}
