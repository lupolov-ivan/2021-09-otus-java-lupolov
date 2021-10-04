package otus.java.lupolov;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;

public class HelloOtus {

    public static void main(String[] args) {

        var greetings = new ArrayList<String>();
        greetings.add("Hello World!!!");
        greetings.add("Hello OTUS!!!");

        ImmutableList<String> immutableGreetings = ImmutableList.copyOf(greetings);

        try {
            immutableGreetings.add("Hello Google!!!");
        }
        catch (UnsupportedOperationException e) {
            System.err.println("Oops... unsupoeted operations");
        }

        immutableGreetings.forEach(System.out::println);
    }
}
