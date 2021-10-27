package otus.java.lupolov.test;

import org.junit.jupiter.api.Test;
import otus.java.lupolov.test.examples.ValidTestClass;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ExecutableTestTest {

    @Test
    void run_when_test_object_has_all_type_of_methods_then_all_methods_call_success() throws Exception {

        var constructor = ValidTestClass.class.getDeclaredConstructor();
        var beforeMethod = ValidTestClass.class.getMethod("setUp");
        var testMethod = ValidTestClass.class.getMethod("test1");
        var afterMethod = ValidTestClass.class.getMethod("tearDown");

        var testObj = new ExecutableTest<>(constructor, beforeMethod, testMethod, afterMethod);
        ExecutableTest<ValidTestClass> testSpy = spy(testObj);

        boolean runTestResult = testSpy.execute();

        assertTrue(runTestResult);

        verify(testSpy, times(1)).callTestMethod(any());
        verify(testSpy, times(2)).callBeforeAfterMethod(any(), any());
        verify(testSpy, times(1)).getInstanceTestClass();
    }

    @Test
    void run_when_test_object_has_only_test_methods_then_before_after_method_never_call() throws Exception {

        var constructor = ValidTestClass.class.getDeclaredConstructor();
        var testMethod = ValidTestClass.class.getMethod("test1");

        var testObj = new ExecutableTest<>(constructor, null, testMethod, null);
        ExecutableTest<ValidTestClass> testSpy = spy(testObj);

        boolean runTestResult = testSpy.execute();

        assertTrue(runTestResult);

        verify(testSpy, times(1)).callTestMethod(any());
        verify(testSpy, never()).callBeforeAfterMethod(any(), any());
    }
}