package otus.java.lupolov;

import otus.java.lupolov.atm.*;
import otus.java.lupolov.model.CashRequestResult;

import java.util.List;

import static otus.java.lupolov.model.Denomination.*;

public class DemoATM {

    public static void main(String[] args) {

        var cassetteHolder = new CassetteHolder(5);
        var billDispenser = new BillDispenser();
        var billSelector = new BillSelector();

        var atm = new ATM(cassetteHolder, billDispenser, billSelector);

        List<BillCassette> cassettes = prepareBillCassettes();
        cassettes.forEach(atm::loadCassette);

        System.out.println("=========== REQUEST #1 =============");
        int beforeBalance = atm.getBalance();
        System.out.println("beforeBalance = " + beforeBalance);

        CashRequestResult requestResult = atm.getCash(3750);

        System.out.println("Status message = " + requestResult.status().message());
        System.out.println("Bill list = " + requestResult.cash());

        int afterBalance = atm.getBalance();
        System.out.println("afterBalance = " + afterBalance);

        System.out.println("=========== REQUEST #2 =============");
        beforeBalance = atm.getBalance();
        System.out.println("beforeBalance = " + beforeBalance);

        requestResult = atm.getCash(330);

        System.out.println("Status message  = " + requestResult.status().message());
        System.out.println("Bill list = " + requestResult.cash());

        afterBalance = atm.getBalance();
        System.out.println("afterBalance = " + afterBalance);
    }

    private static List<BillCassette> prepareBillCassettes() {

        var cassette1 = BillCassette.createCassette(ONE_THOUSAND, 10);
        var cassette2 = BillCassette.createCassette(FIVE_HUNDRED, 10);
        var cassette3 = BillCassette.createCassette(TWO_HUNDRED, 10);
        var cassette4 = BillCassette.createCassette(ONE_HUNDRED, 10);
        var cassette5 = BillCassette.createCassette(FIFTY, 10);

        return List.of(cassette1, cassette2, cassette3, cassette4, cassette5);
    }
}
