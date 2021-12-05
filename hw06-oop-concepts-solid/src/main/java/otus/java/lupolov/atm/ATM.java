package otus.java.lupolov.atm;

import otus.java.lupolov.model.*;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

public class ATM {

    private final CassetteHolder cassetteHolder;
    private final BillDispenser billDispenser;
    private final BillSelector billSelector;

    public ATM(CassetteHolder cassetteHolder, BillDispenser billDispenser, BillSelector billSelector) {
        this.cassetteHolder = cassetteHolder;
        this.billDispenser = billDispenser;
        this.billSelector = billSelector;
    }

    public CashRequestResult getCash(int sum) {

        int balance = cassetteHolder.getAvailableCashBalance();
        Set<Denomination> availableDenominations = cassetteHolder.getAvailableDenominations();

        SelectionResult selectionResult
                = billSelector.selectBills(sum, balance, availableDenominations, billDispenser::getDenominationBalance);

        if (selectionResult.status() == Status.SUCCESS) {
            List<Bill> requestedCash = billDispenser.dispenseBills(selectionResult.billsToTake());
            return new CashRequestResult(selectionResult.status(), requestedCash);
        }

        return new CashRequestResult(selectionResult.status(), emptyList());
    }

    public void loadCassette(BillCassette cassette) {
        cassetteHolder.loadCassette(cassette);
        billDispenser.readCellsInformation(cassetteHolder.getCassettes());
    }

    public void extractCassette(BillCassette cassette) {
        cassetteHolder.extractCassette(cassette);
        billDispenser.readCellsInformation(cassetteHolder.getCassettes());
    }

    public int getBalance() {
        return cassetteHolder.getAvailableCashBalance();
    }
}
