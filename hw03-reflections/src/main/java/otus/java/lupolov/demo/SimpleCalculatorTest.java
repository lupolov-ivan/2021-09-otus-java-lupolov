package otus.java.lupolov.demo;

import otus.java.lupolov.test.annotation.After;
import otus.java.lupolov.test.annotation.Before;
import otus.java.lupolov.test.annotation.Test;

public class SimpleCalculatorTest {

    private SimpleCalculator calculator;

    @Before
    public void setUp() {
        calculator = new SimpleCalculator(1, 0);
        System.out.println("Set up calculator = " + calculator);
    }

    @Test
    public void test1() {
        int result = calculator.add();
        System.out.println("Add result = " + result);
    }

    @Test
    public void test2() {
         int result = calculator.sub();
        System.out.println("Sub result = " + result);
    }

    @Test
    public void test3() {
         int result = calculator.divide();
        System.out.println("Divide result = " + result);
    }

    @Test
    public void test4() {
         int result = calculator.multiply();
        System.out.println("Multiply result = " + result);
    }


    @After
    public void tearDown() {
        calculator = null;
        System.out.println("Tear down called");
    }
}
