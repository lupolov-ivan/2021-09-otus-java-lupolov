package otus.java.lupolov.atm;

import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

import java.util.ArrayList;
import java.util.List;

public class BillCassette {

    private final Denomination denomination;
    private final List<Bill> bills;

    private BillCassette(Denomination denomination, List<Bill> bills) {
        this.denomination = denomination;
        this.bills = bills;
    }

    public static BillCassette createCassette(Denomination denomination, int capacity) {

        List<Bill> bills = new ArrayList<>();

        for (int i = 0; i < capacity; i++) {
            bills.add(new Bill(denomination));
        }

        return new BillCassette(denomination, bills);
    }

    public Bill extractBill() {
        if (bills.isEmpty()) {
            return null;
        }
        return bills.remove(0);
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getBalance() {
        return bills.size();
    }
}
