package otus.java.lupolov;

import java.util.ArrayList;
import java.util.List;

public class Cell {

    private BillDenomination denomination;
    private int capacity;
    private final List<Bill> bills;

    public Cell(int capacity, List<Bill> bills) {
        this.capacity = capacity;

        if(bills.size() > capacity) {
            throw new IllegalArgumentException("Number of bill can't grater than capacity");
        }
        this.bills = bills;
    }

    public List<Bill> takeOutBills(int quantity) {

        if (bills.size() < quantity) {
            throw new IllegalArgumentException("Cell contains an insufficient number of bills");
        }

        List<Bill> result = new ArrayList<>(quantity);

        for (int i = 0; i < quantity; i++) {
            result.add(bills.remove(0));
        }

        return result;
    }

    public BillDenomination getDenomination() {
        return denomination;
    }

    public int getBalance() {
        return bills.size();
    }
}
