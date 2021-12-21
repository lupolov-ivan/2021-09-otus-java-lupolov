package otus.java.lupolov.atm;

import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

public class BillCassette {

    private final Denomination denomination;
    private Integer balance;

    public BillCassette(Denomination denomination, Integer balance) {
        this.denomination = denomination;
        this.balance = balance;
    }

    public Bill extractBill() {
        balance--;
        if (balance == 0) {
            return null;
        }
        return new Bill(denomination);
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getBalance() {
        return balance;
    }
}
