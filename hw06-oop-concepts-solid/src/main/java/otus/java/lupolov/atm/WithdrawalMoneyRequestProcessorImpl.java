package otus.java.lupolov.atm;

import otus.java.lupolov.model.Denomination;
import otus.java.lupolov.model.WithdrawalRequestResult;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static otus.java.lupolov.model.Status.FAILED;
import static otus.java.lupolov.model.Status.SUCCESS;

public class WithdrawalMoneyRequestProcessorImpl implements WithdrawalMoneyRequestProcessor {

    private final CassetteHolder cassetteHolder;

    public WithdrawalMoneyRequestProcessorImpl(CassetteHolder cassetteHolder) {
        this.cassetteHolder = cassetteHolder;
    }

    @Override
    public WithdrawalRequestResult processWithdrawalRequest(int sum) {

        Set<Denomination> availableDenominations = cassetteHolder.getAvailableDenominations();

        if (availableDenominations.isEmpty()) {
            return new WithdrawalRequestResult(FAILED, "The ATM is out of money", emptyMap());
        }

        if (sum > cassetteHolder.getAvailableCashBalance()) {
            return new WithdrawalRequestResult(FAILED, "There is not enough money in the ATM", emptyMap());
        }

        TreeSet<Denomination> denominations = availableDenominations.stream()
                .sorted(Comparator.comparing(Denomination::value))
                .collect(Collectors.toCollection(TreeSet::new));

        int minDenominationValue = denominations.first().value();

        if (sum % minDenominationValue != 0) {
            var infoMessage = "The requested amount is not a multiple of the minimum denomination";
            return new WithdrawalRequestResult(FAILED, infoMessage, emptyMap());
        }

        int remainingSum  = sum;

        Map<Denomination, Integer> denominationToQuantityMap = new HashMap<>();
        Iterator<Denomination> denominationIterator = denominations.descendingIterator();

        while (remainingSum  > 0 && denominationIterator.hasNext()) {

            Denomination denomination = denominationIterator.next();
            int denominationValue = denomination.value();

            if(remainingSum  < denominationValue) {
                continue;
            }

            int requiredNumBillsByDenomination = remainingSum / denominationValue;
            int cellBillBalance = cassetteHolder.getBillBalanceByDenomination(denomination);

            if (cellBillBalance > requiredNumBillsByDenomination) {
                denominationToQuantityMap.put(denomination, requiredNumBillsByDenomination);
                remainingSum  -= requiredNumBillsByDenomination * denominationValue;
            } else {
                denominationToQuantityMap.put(denomination, cellBillBalance);
                remainingSum  -= cellBillBalance * denominationValue;
            }
        }

        if (remainingSum  != 0) {
            var infoMessage = "It is impossible to issue the requested amount from the remaining bills";
            return new WithdrawalRequestResult(FAILED, infoMessage, emptyMap());
        }

        return new WithdrawalRequestResult(SUCCESS, "Request success", denominationToQuantityMap);
    }
}
