package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Denomination;
import otus.java.lupolov.model.WithdrawalRequestResult;

import java.util.Map;
import java.util.Set;

import static java.util.Collections.EMPTY_MAP;
import static java.util.Collections.EMPTY_SET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static otus.java.lupolov.model.Denomination.ONE_HUNDRED;
import static otus.java.lupolov.model.Denomination.ONE_THOUSAND;
import static otus.java.lupolov.model.Status.FAILED;
import static otus.java.lupolov.model.Status.SUCCESS;

class WithdrawalMoneyRequestProcessorImplTest {

    WithdrawalMoneyRequestProcessor withdrawalMoneyRequestProcessor;
    CassetteHolder cassetteHolderMock;

    @BeforeEach
    void setUp() {
        cassetteHolderMock = mock(CassetteHolder.class);
        withdrawalMoneyRequestProcessor = new WithdrawalMoneyRequestProcessorImpl(cassetteHolderMock);
    }

    @Test
    void processWithdrawalRequest_whenCassetteHolderReturnEmptyDenominationsSet_thenReturnStatusFailed() {

        when(cassetteHolderMock.getAvailableDenominations()).thenReturn(EMPTY_SET);

        WithdrawalRequestResult actualResult = withdrawalMoneyRequestProcessor.processWithdrawalRequest(100);

        assertEquals(FAILED, actualResult.status());
        assertEquals(EMPTY_MAP, actualResult.denominationToQuantityMap());

        verify(cassetteHolderMock, times(1)).getAvailableDenominations();
    }

    @Test
    void processWithdrawalRequest_whenCassetteHolderReturnBalanceLessThanRequestedSum_thenReturnStatusFailed() {

        when(cassetteHolderMock.getAvailableDenominations()).thenReturn(Set.of(ONE_HUNDRED));

        var totalCashBalance = 100;
        when(cassetteHolderMock.getAvailableCashBalance()).thenReturn(totalCashBalance);

        var requestedSum = 300;
        WithdrawalRequestResult actualResult = withdrawalMoneyRequestProcessor.processWithdrawalRequest(requestedSum);

        assertEquals(FAILED, actualResult.status());
        assertEquals(EMPTY_MAP, actualResult.denominationToQuantityMap());

        verify(cassetteHolderMock, times(1)).getAvailableDenominations();
        verify(cassetteHolderMock, times(1)).getAvailableCashBalance();
    }

    @Test
    void processWithdrawalRequest_whenRequestedSumNotMultipleOfMinDenomination_thenReturnStatusFailed() {

        when(cassetteHolderMock.getAvailableDenominations()).thenReturn(Set.of(ONE_HUNDRED));

        var totalCashBalance = 1000;
        when(cassetteHolderMock.getAvailableCashBalance()).thenReturn(totalCashBalance);

        var requestedSum = 330;
        WithdrawalRequestResult actualResult = withdrawalMoneyRequestProcessor.processWithdrawalRequest(requestedSum);

        assertEquals(FAILED, actualResult.status());
        assertEquals(EMPTY_MAP, actualResult.denominationToQuantityMap());

        verify(cassetteHolderMock, times(1)).getAvailableDenominations();
        verify(cassetteHolderMock, times(1)).getAvailableCashBalance();
    }

    @Test
    void processWithdrawalRequest_whenRemainingBillsCantIssueRequestedAmount_thenReturnStatusFailed() {

        when(cassetteHolderMock.getAvailableDenominations()).thenReturn(Set.of(ONE_HUNDRED, ONE_THOUSAND));

        var totalCashBalance = 1100;
        when(cassetteHolderMock.getAvailableCashBalance()).thenReturn(totalCashBalance);

        when(cassetteHolderMock.getBillBalanceByDenomination(ONE_HUNDRED)).thenReturn(1);
        when(cassetteHolderMock.getBillBalanceByDenomination(ONE_THOUSAND)).thenReturn(1);

        var requestedSum = 200;
        WithdrawalRequestResult actualResult = withdrawalMoneyRequestProcessor.processWithdrawalRequest(requestedSum);

        assertEquals(FAILED, actualResult.status());
        assertEquals(EMPTY_MAP, actualResult.denominationToQuantityMap());

        verify(cassetteHolderMock, times(1)).getAvailableDenominations();
        verify(cassetteHolderMock, times(1)).getAvailableCashBalance();
        verify(cassetteHolderMock, times(1)).getBillBalanceByDenomination(ONE_HUNDRED);
        verify(cassetteHolderMock, never()).getBillBalanceByDenomination(ONE_THOUSAND);
    }

    @Test
    void processWithdrawalRequest_whenEnoughMoney_thenReturnStatusSuccess() {

        when(cassetteHolderMock.getAvailableDenominations()).thenReturn(Set.of(ONE_HUNDRED, ONE_THOUSAND));

        var totalCashBalance = 2100;
        when(cassetteHolderMock.getAvailableCashBalance()).thenReturn(totalCashBalance);

        when(cassetteHolderMock.getBillBalanceByDenomination(ONE_HUNDRED)).thenReturn(1);
        when(cassetteHolderMock.getBillBalanceByDenomination(ONE_THOUSAND)).thenReturn(2);

        var requestedSum = 1100;
        WithdrawalRequestResult actualResult = withdrawalMoneyRequestProcessor.processWithdrawalRequest(requestedSum);

        Map<Denomination, Integer> expectedMap = Map.of(ONE_HUNDRED, 1, ONE_THOUSAND, 1);

        assertEquals(SUCCESS, actualResult.status());
        assertEquals(expectedMap, actualResult.denominationToQuantityMap());

        verify(cassetteHolderMock, times(1)).getAvailableDenominations();
        verify(cassetteHolderMock, times(1)).getAvailableCashBalance();
        verify(cassetteHolderMock, times(1)).getBillBalanceByDenomination(ONE_HUNDRED);
        verify(cassetteHolderMock, times(1)).getBillBalanceByDenomination(ONE_THOUSAND);
    }
}