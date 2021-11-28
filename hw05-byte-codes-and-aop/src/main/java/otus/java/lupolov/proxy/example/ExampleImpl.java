package otus.java.lupolov.proxy.example;

import otus.java.lupolov.Log;

public class ExampleImpl implements Example {

    @Override
    public void test(String s1) {
        System.out.println("Method without logging");
    }

    @Log
    @Override
    public void test(String s1, int i1) {
        System.out.println("Method with logging");
    }

    @Log
    @Override
    public int test2(int op1, int op2) {
        System.out.println("Method with logging");
        return op1 + op2;
    }
}
