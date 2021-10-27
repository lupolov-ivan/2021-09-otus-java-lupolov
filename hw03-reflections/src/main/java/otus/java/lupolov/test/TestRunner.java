package otus.java.lupolov.test;

import java.util.List;

public class TestRunner {

    private TestRunner() {
    }

    public static <T> void runTest(Class<T> testClass) {

        var testAnnotationScanner = new TestAnnotationScanner();
        var testMethodBuilder = new TestMethodBuilder<T>(testAnnotationScanner);

        List<ExecutableTest<T>> tests = testMethodBuilder.buildAndGetTests(testClass);

        String startMessage = """
                ====================================   TEST STARTING   ====================================
                Class: %s. Total tests: %d
                ===========================================================================================%n""";
        System.out.printf(startMessage, testClass.getName(), tests.size());

        int successTestCount = 0;
        int failedTestCount = 0;

        for (ExecutableTest<?> test : tests) {
            boolean runTestResult = test.execute();
            if (runTestResult) {
                successTestCount++;
            }
            else {
                failedTestCount++;
            }
        }

        String endMessage = """
                ======================================  TEST RESULT  ======================================
                Success: %d. Failed: %d 
                ===========================================================================================""";
        System.out.printf(endMessage, successTestCount, failedTestCount);
    }
}
