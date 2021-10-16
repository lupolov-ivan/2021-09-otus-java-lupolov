package otus.java.lupolov.customer;


import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> customers = new LinkedList<>();

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        return customers.removeLast();
    }
}
