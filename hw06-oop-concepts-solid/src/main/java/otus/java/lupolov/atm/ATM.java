package otus.java.lupolov.atm;

import otus.java.lupolov.model.CashRequestResult;

public interface ATM {

    CashRequestResult withdrawMoney(int sum);

    boolean loadCassette(BillCassette cassette);

    boolean extractCassette(BillCassette cassette);

    int getBalance();
}
