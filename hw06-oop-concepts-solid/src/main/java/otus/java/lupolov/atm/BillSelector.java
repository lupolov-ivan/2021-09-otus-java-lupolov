package otus.java.lupolov.atm;

import otus.java.lupolov.model.Denomination;
import otus.java.lupolov.model.SelectionResult;

import java.util.*;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;
import static otus.java.lupolov.model.Status.*;


public class BillSelector {

    public SelectionResult selectBills(int sum,
                                       int balance,
                                       Set<Denomination> availableDenominations,
                                       ToIntFunction<Denomination> checkDenominationBalanceFunction) {

        if (availableDenominations.isEmpty()) {
            return new SelectionResult(NO_MONEY, emptyMap());
        }

        if (sum > balance) {
            return new SelectionResult(NOT_ENOUGH_MONEY, emptyMap());
        }

        TreeSet<Denomination> denominations = availableDenominations.stream()
                .sorted(Comparator.comparing(Denomination::value))
                .collect(Collectors.toCollection(TreeSet::new));

        int minDenominationValue = denominations.first().value();

        if (sum % minDenominationValue != 0) {
            return new SelectionResult(NOT_MULTIPLE_MIN_DENOMINATION, emptyMap());
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
            int cellBillBalance = checkDenominationBalanceFunction.applyAsInt(denomination);

            if (cellBillBalance > requiredNumBillsByDenomination) {
                denominationToQuantityMap.put(denomination, requiredNumBillsByDenomination);
                remainingSum  -= requiredNumBillsByDenomination * denominationValue;
            } else {
                denominationToQuantityMap.put(denomination, cellBillBalance);
                remainingSum  -= cellBillBalance * denominationValue;
            }
        }

        if (remainingSum  != 0) {
            return new SelectionResult(IMPOSSIBLE_ISSUE_AMOUNT_FROM_REMAINING_BILLS,  emptyMap());
        }

        return new SelectionResult(SUCCESS,  denominationToQuantityMap);
    }
}
