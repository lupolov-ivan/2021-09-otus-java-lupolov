package otus.java.lupolov.test;

import otus.java.lupolov.test.data.TestClassData;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestMethodBuilder<T> {

    private final TestAnnotationScanner annotationScanner;

    public TestMethodBuilder(TestAnnotationScanner annotationScanner) {
        this.annotationScanner = annotationScanner;
    }

    public List<ExecutableTest<T>> buildAndGetTests(Class<T> testClass) {

        List<ExecutableTest<T>> result = new ArrayList<>();
        TestClassData<T> testClassData = annotationScanner.scanClass(testClass);

        for (Method testMethod : testClassData.getTestMethods()) {

            var test = new ExecutableTest<>(
                    testClassData.getConstructor(),
                    testClassData.getBeforeMethod(),
                    testMethod,
                    testClassData.getAfterMethod()
            );

            result.add(test);
        }

        return result;
    }
}
