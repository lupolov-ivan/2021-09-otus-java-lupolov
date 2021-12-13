package otus.java.lupolov;

import otus.java.lupolov.atm.*;
import otus.java.lupolov.model.CashRequestResult;

import static otus.java.lupolov.model.Denomination.*;

public class DemoATM {

    public static final String REQUEST_SUM_TEMPLATE = "=========== REQUEST (sum = %s) =============%n";

    public static void main(String[] args) {

        var cassetteHolder = new CassetteHolderImpl();
        var withdrawalMoneyRequestProcessor = new WithdrawalMoneyRequestProcessorImpl(cassetteHolder);
        var billDispenser = new BillDispenserImpl(cassetteHolder);

        var atm = new ATMImpl(cassetteHolder, billDispenser, withdrawalMoneyRequestProcessor);

        var cassette1 = new BillCassette(ONE_THOUSAND, 10);
        var cassette2 = new BillCassette(FIVE_HUNDRED, 10);
        var cassette3 = new BillCassette(TWO_HUNDRED, 10);
        var cassette4 = new BillCassette(ONE_HUNDRED, 10);
        var cassette5 = new BillCassette(FIFTY, 10);

        atm.loadCassette(cassette1);
        atm.loadCassette(cassette2);
        atm.loadCassette(cassette3);
        atm.loadCassette(cassette4);
        atm.loadCassette(cassette5);

        int sum = 3750;
        System.out.printf(REQUEST_SUM_TEMPLATE, sum);
        int beforeBalance = atm.getBalance();
        System.out.println("beforeBalance = " + beforeBalance);

        CashRequestResult requestResult = atm.withdrawMoney(sum);

        System.out.println("Status = " + requestResult.status());
        System.out.println("Info message = " + requestResult.infoMessage());
        System.out.println("Bill list = " + requestResult.cash());

        int afterBalance = atm.getBalance();
        System.out.println("afterBalance = " + afterBalance);

        sum = 300;
        System.out.printf(REQUEST_SUM_TEMPLATE, sum);
        beforeBalance = atm.getBalance();
        System.out.println("beforeBalance = " + beforeBalance);

        requestResult = atm.withdrawMoney(sum);

        System.out.println("Status = " + requestResult.status());
        System.out.println("Info message = " + requestResult.infoMessage());
        System.out.println("Bill list = " + requestResult.cash());

        afterBalance = atm.getBalance();
        System.out.println("afterBalance = " + afterBalance);

        sum = 2250;
        System.out.printf(REQUEST_SUM_TEMPLATE, sum);
        beforeBalance = atm.getBalance();
        System.out.println("beforeBalance = " + beforeBalance);

        atm.extractCassette(cassette1);

        beforeBalance = atm.getBalance();
        System.out.println("balance after extracting cassette with 1000 banknotes = " + beforeBalance);

        requestResult = atm.withdrawMoney(sum);

        System.out.println("Status = " + requestResult.status());
        System.out.println("Info message = " + requestResult.infoMessage());
        System.out.println("Bill list = " + requestResult.cash());

        afterBalance = atm.getBalance();
        System.out.println("afterBalance = " + afterBalance);
    }
}
