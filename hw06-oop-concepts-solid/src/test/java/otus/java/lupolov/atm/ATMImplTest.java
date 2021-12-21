package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.CashRequestResult;
import otus.java.lupolov.model.WithdrawalRequestResult;

import java.util.List;
import java.util.Map;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.EMPTY_MAP;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static otus.java.lupolov.model.Denomination.ONE_THOUSAND;
import static otus.java.lupolov.model.Status.FAILED;
import static otus.java.lupolov.model.Status.SUCCESS;

class ATMImplTest {

    CassetteHolder cassetteHolderMock;
    BillDispenser billDispenserMock;
    WithdrawalMoneyRequestProcessor withdrawalMoneyRequestProcessorMock;

    ATM atm;

    @BeforeEach
    void setUp() {
        cassetteHolderMock = mock(CassetteHolder.class);
        billDispenserMock = mock(BillDispenser.class);
        withdrawalMoneyRequestProcessorMock = mock(WithdrawalMoneyRequestProcessor.class);

        atm = new ATMImpl(cassetteHolderMock, billDispenserMock, withdrawalMoneyRequestProcessorMock);
    }

    @Test
    void withdrawMoney_whenWithdrawalRequestResultSuccess_thenReturnSelectedBills() {

        var requestedSum = 1000;
        WithdrawalRequestResult requestResult
                = new WithdrawalRequestResult(SUCCESS, "Success", Map.of(ONE_THOUSAND, 1));

        when(withdrawalMoneyRequestProcessorMock.processWithdrawalRequest(requestedSum)).thenReturn(requestResult);

        var expectedBills = List.of(new Bill(ONE_THOUSAND));
        when(billDispenserMock.dispense(requestResult.denominationToQuantityMap())).thenReturn(expectedBills);

        CashRequestResult cashRequestResult = atm.withdrawMoney(requestedSum);

        assertEquals(SUCCESS, cashRequestResult.status());
        assertEquals(expectedBills, cashRequestResult.cash());

        verify(withdrawalMoneyRequestProcessorMock, times(1)).processWithdrawalRequest(requestedSum);
        verify(billDispenserMock, times(1)).dispense(requestResult.denominationToQuantityMap());
    }

    @Test
    void withdrawMoney_whenWithdrawalRequestResultFailed_thenReturnEmptyBillList() {

        var requestedSum = 1000;
        WithdrawalRequestResult requestResult = new WithdrawalRequestResult(FAILED, "Failed", EMPTY_MAP);

        when(withdrawalMoneyRequestProcessorMock.processWithdrawalRequest(requestedSum)).thenReturn(requestResult);

        CashRequestResult cashRequestResult = atm.withdrawMoney(requestedSum);

        assertEquals(FAILED, cashRequestResult.status());
        assertEquals(EMPTY_LIST, cashRequestResult.cash());

        verify(withdrawalMoneyRequestProcessorMock, times(1)).processWithdrawalRequest(requestedSum);
    }

    @Test
    void loadCassette() {

        var cassette = new BillCassette(ONE_THOUSAND, 1);

        when(cassetteHolderMock.loadCassette(cassette)).thenReturn(true);

        assertTrue(atm.loadCassette(cassette));

        verify(cassetteHolderMock, times(1)).loadCassette(cassette);
    }

    @Test
    void extractCassette() {

        var cassette = new BillCassette(ONE_THOUSAND, 1);

        when(cassetteHolderMock.extractCassette(cassette)).thenReturn(false);

        assertFalse(atm.extractCassette(cassette));

        verify(cassetteHolderMock, times(1)).extractCassette(cassette);
    }

    @Test
    void getBalance() {

        int expectedCashBalance = 100;
        when(cassetteHolderMock.getAvailableCashBalance()).thenReturn(expectedCashBalance);

        assertEquals(expectedCashBalance, atm.getBalance());

        verify(cassetteHolderMock, times(1)).getAvailableCashBalance();
    }
}