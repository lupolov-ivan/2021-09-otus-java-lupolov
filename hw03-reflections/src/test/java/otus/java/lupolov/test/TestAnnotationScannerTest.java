package otus.java.lupolov.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.test.data.TestClassData;
import otus.java.lupolov.test.examples.DoubleBeforeMethodTestClass;
import otus.java.lupolov.test.examples.NonZeroArgsConstructorTestClass;
import otus.java.lupolov.test.examples.ValidTestClass;

import static org.junit.jupiter.api.Assertions.*;

class TestAnnotationScannerTest {

    TestAnnotationScanner annotationScanner;

    @BeforeEach
    void setUp() {
        annotationScanner = new TestAnnotationScanner();
    }

    @Test
    void scanClass_when_passed_valid_test_class_then_scanning_success() {
        TestClassData<ValidTestClass> actualResult = annotationScanner.scanClass(ValidTestClass.class);

        assertNotNull(actualResult.getBeforeMethod());
        assertEquals(2, actualResult.getTestMethods().size());
        assertNotNull(actualResult.getAfterMethod());
    }

    @Test
    void scanClass_when_passed_test_class_with_two_before_methods_then_throw_exception() {
        IllegalStateException actualException
                = assertThrows(IllegalStateException.class, () -> annotationScanner.scanClass(DoubleBeforeMethodTestClass.class));

        var expectedExceptionMessage = "Test class must contain only one method marked annotation '@Before'";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }

    @Test
    void scanClass_when_passed_test_class_dont_have_zer_args_constructor_then_throw_exception() {
        IllegalStateException actualException
                = assertThrows(IllegalStateException.class, () -> annotationScanner.scanClass(NonZeroArgsConstructorTestClass.class));

        var expectedExceptionMessage = "Test class must have zero-args constructor";
        assertEquals(expectedExceptionMessage, actualException.getMessage());
    }
}