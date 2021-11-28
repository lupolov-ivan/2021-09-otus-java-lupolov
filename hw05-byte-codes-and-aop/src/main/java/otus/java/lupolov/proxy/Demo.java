package otus.java.lupolov.proxy;

import otus.java.lupolov.proxy.example.Example;
import otus.java.lupolov.proxy.example.ExampleImpl;

public class Demo {

    public static void main(String[] args) {

        Example proxyInstance = LoggingProxyCreator.getProxyInstance(Example.class, new ExampleImpl());

        proxyInstance.test("test one");
        proxyInstance.test("test two", 2);
        proxyInstance.test2(3, 2);
    }
}
