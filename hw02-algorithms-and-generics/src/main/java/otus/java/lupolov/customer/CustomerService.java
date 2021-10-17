package otus.java.lupolov.customer;


import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparing;

public class CustomerService {

    private final TreeMap<Customer, String> customerToData = new TreeMap<>(comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return customerToData.firstEntry();
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return customerToData.higherEntry(customer);
    }

    public void add(Customer customer, String data) {
        customerToData.put(customer, data);
    }
}
