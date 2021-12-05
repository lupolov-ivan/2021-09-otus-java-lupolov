package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Denomination;
import otus.java.lupolov.model.SelectionResult;

import java.util.Map;
import java.util.Set;
import java.util.function.ToIntFunction;

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.*;
import static otus.java.lupolov.model.Denomination.*;
import static otus.java.lupolov.model.Status.*;

class BillSelectorTest {

    BillSelector billSelector;
    ToIntFunction<Denomination> checkDenominationBalanceFunction;
    Set<Denomination> availableDenominations;

    @BeforeEach
    void setUp() {
        billSelector = new BillSelector();

        checkDenominationBalanceFunction = denomination -> switch (denomination) {
            case ONE_THOUSAND -> 2;
            case  FIFTY -> 1;
            case TWO_HUNDRED, ONE_HUNDRED, FIVE_HUNDRED -> 0;
        };

        availableDenominations = Set.of(ONE_THOUSAND, FIFTY);
    }

    @Test
    void selectBills_atmNoMoney_returnNoMoneyError() {

        var requestSum = 500;
        var balance = 0;

        SelectionResult selectionResult
                = billSelector.selectBills(requestSum, balance, emptySet(), checkDenominationBalanceFunction);

        assertEquals(NO_MONEY, selectionResult.status());
        assertTrue(selectionResult.billsToTake().isEmpty());
    }

    @Test
    void selectBills_sumGreaterAtmBalance_returnNotEnoughMoneyError() {

        var requestSum = 1500;
        var balance = 1050;

        SelectionResult selectionResult
                = billSelector.selectBills(requestSum, balance, availableDenominations, checkDenominationBalanceFunction);

        assertEquals(NOT_ENOUGH_MONEY, selectionResult.status());
        assertTrue(selectionResult.billsToTake().isEmpty());
    }

    @Test
    void selectBills_sumNotMultiMinDenomination_returnNotMultiMinDenominationError() {

        var requestSum = 330;
        var balance = 1050;

        SelectionResult selectionResult
                = billSelector.selectBills(requestSum, balance, availableDenominations, checkDenominationBalanceFunction);

        assertEquals(NOT_MULTIPLE_MIN_DENOMINATION, selectionResult.status());
        assertTrue(selectionResult.billsToTake().isEmpty());
    }


    @Test
    void selectBills_sumImpossibleIssueAmountFromRemainingBills_returnImpossibleIssueAmountFromRemainingBillsError() {

        var requestSum = 100;
        var balance = 1050;

        SelectionResult selectionResult
                = billSelector.selectBills(requestSum, balance, availableDenominations, checkDenominationBalanceFunction);

        assertEquals(IMPOSSIBLE_ISSUE_AMOUNT_FROM_REMAINING_BILLS, selectionResult.status());
        assertTrue(selectionResult.billsToTake().isEmpty());
    }

    @Test
    void selectBills_atmHasEnoughMoney_returnRequestedBillsMap() {

        var requestSum = 1050;
        var balance = 1050;

        SelectionResult selectionResult
                = billSelector.selectBills(requestSum, balance, availableDenominations, checkDenominationBalanceFunction);

        assertEquals(SUCCESS, selectionResult.status());

        Map<Denomination, Integer> actualDenominationIntegerMap = selectionResult.billsToTake();

        assertEquals(2, actualDenominationIntegerMap.size());
        assertEquals(1, actualDenominationIntegerMap.get(ONE_THOUSAND));
        assertEquals(1, actualDenominationIntegerMap.get(FIFTY));
    }
}