package otus.java.lupolov.test;

import org.junit.jupiter.api.Test;
import otus.java.lupolov.test.data.TestClassData;
import otus.java.lupolov.test.examples.ValidTestClass;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestMethodBuilderTest {

    @Test
    void buildAndGetTests() throws NoSuchMethodException {

        TestAnnotationScanner mockTestAnnotationScanner = mock(TestAnnotationScanner.class);
        var testMethodBuilder = new TestMethodBuilder<ValidTestClass>(mockTestAnnotationScanner);
        var mockConstructor = ValidTestClass.class.getDeclaredConstructor();
        var mockMethod = ValidTestClass.class.getMethod("setUp");

        var testClassData = new TestClassData<ValidTestClass>();
        testClassData.setConstructor(mockConstructor);
        testClassData.setBeforeMethod(mockMethod);
        testClassData.setTestMethods(List.of(mockMethod, mockMethod));
        testClassData.setAfterMethod(mockMethod);

        when(mockTestAnnotationScanner.scanClass(ValidTestClass.class)).thenReturn(testClassData);

        List<ExecutableTest<ValidTestClass>> actualResult = testMethodBuilder.buildAndGetTests(ValidTestClass.class);

        assertEquals(2, actualResult.size());

        verify(mockTestAnnotationScanner, times(1)).scanClass(ValidTestClass.class);
    }
}