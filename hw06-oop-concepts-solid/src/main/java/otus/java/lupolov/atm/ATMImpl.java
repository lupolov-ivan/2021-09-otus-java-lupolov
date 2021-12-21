package otus.java.lupolov.atm;

import otus.java.lupolov.model.*;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class ATMImpl implements ATM {

    private final BillDispenser billDispenser;
    private final CassetteHolder cassetteHolder;
    private final WithdrawalMoneyRequestProcessor withdrawalMoneyRequestProcessor;

    public ATMImpl(CassetteHolder cassetteHolder,
                   BillDispenser billDispenser,
                   WithdrawalMoneyRequestProcessor withdrawalMoneyRequestProcessor) {
        this.billDispenser = billDispenser;
        this.cassetteHolder = cassetteHolder;
        this.withdrawalMoneyRequestProcessor = withdrawalMoneyRequestProcessor;
    }

    public CashRequestResult withdrawMoney(int sum) {

        WithdrawalRequestResult requestResult = withdrawalMoneyRequestProcessor.processWithdrawalRequest(sum);

        if (requestResult.status() == Status.SUCCESS) {
            Map<Denomination, Integer> denominationToQuantityMap = requestResult.denominationToQuantityMap();
            List<Bill> bills = billDispenser.dispense(denominationToQuantityMap);
            return new CashRequestResult(requestResult.status(), requestResult.infoMessage(), bills);
        }

        return new CashRequestResult(requestResult.status(), requestResult.infoMessage(), emptyList());
    }

    public boolean loadCassette(BillCassette cassette) {
        return cassetteHolder.loadCassette(cassette);
    }

    public boolean extractCassette(BillCassette cassette) {
        return cassetteHolder.extractCassette(cassette);
    }

    public int getBalance() {
        return cassetteHolder.getAvailableCashBalance();
    }
}
