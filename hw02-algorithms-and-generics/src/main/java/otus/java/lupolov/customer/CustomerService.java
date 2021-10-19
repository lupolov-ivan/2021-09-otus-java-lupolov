package otus.java.lupolov.customer;


import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Comparator.comparing;

public class CustomerService {

    private final TreeMap<Customer, String> customerToData = new TreeMap<>(comparing(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return copyOf(customerToData.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyOf(customerToData.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customerToData.put(customer, data);
    }

    private Map.Entry<Customer, String> copyOf(Map.Entry<Customer, String> customerStringEntry) {
        if (customerStringEntry == null) {
            return null;
        }

        var realCustomer = customerStringEntry.getKey();
        var copyCustomer = new Customer(realCustomer.getId(), realCustomer.getName(), realCustomer.getScores());

        return new AbstractMap.SimpleImmutableEntry<>(copyCustomer, customerStringEntry.getValue());
    }
}
