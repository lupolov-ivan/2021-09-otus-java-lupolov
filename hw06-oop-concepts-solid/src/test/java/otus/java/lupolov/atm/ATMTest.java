package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.CashRequestResult;
import otus.java.lupolov.model.Denomination;
import otus.java.lupolov.model.SelectionResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static otus.java.lupolov.model.Denomination.*;
import static otus.java.lupolov.model.Status.*;

class ATMTest {

    ATM atm;

    CassetteHolder mockCassetteHolder;
    BillDispenser mockBillDispenser;
    BillSelector mockBillSelector;

    @BeforeEach
    void setUp() {

        mockCassetteHolder = mock(CassetteHolder.class);
        mockBillDispenser = mock(BillDispenser.class);
        mockBillSelector = mock(BillSelector.class);

        atm = new ATM(mockCassetteHolder, mockBillDispenser, mockBillSelector);
    }

    @Test
    void getCash_billSelectorReturnError_returnError() {

        var availableBalance = 100;
        when(mockCassetteHolder.getAvailableCashBalance()).thenReturn(availableBalance);
        when(mockCassetteHolder.getAvailableDenominations()).thenReturn(Set.of(ONE_HUNDRED));

        var mockSelectionResult = new SelectionResult(NOT_ENOUGH_MONEY, emptyMap());
        when(mockBillSelector.selectBills(anyInt(), anyInt(), any(), any())).thenReturn(mockSelectionResult);

        CashRequestResult cashRequestResult = atm.getCash(200);

        assertEquals(mockSelectionResult.status(), cashRequestResult.status());
        assertTrue(cashRequestResult.cash().isEmpty());

        verify(mockCassetteHolder, times(1)).getAvailableCashBalance();
        verify(mockCassetteHolder, times(1)).getAvailableDenominations();
        verify(mockBillSelector, times(1)).selectBills(anyInt(), anyInt(), any(), any());
    }

    @Test
    void getCash_billSelectorReturnSuccess_returnError() {

        var availableBalance = 1000;
        when(mockCassetteHolder.getAvailableCashBalance()).thenReturn(availableBalance);
        when(mockCassetteHolder.getAvailableDenominations()).thenReturn(Set.of(ONE_HUNDRED));

        Map<Denomination, Integer> denominationToQuantityMap = Map.of(ONE_HUNDRED, 1);
        var mockSelectionResult = new SelectionResult(SUCCESS, denominationToQuantityMap);
        when(mockBillSelector.selectBills(anyInt(), anyInt(), any(), any())).thenReturn(mockSelectionResult);

        when(mockBillDispenser.dispenseBills(denominationToQuantityMap)).thenReturn(List.of(new Bill(ONE_HUNDRED)));

        CashRequestResult cashRequestResult = atm.getCash(200);

        assertEquals(mockSelectionResult.status(), cashRequestResult.status());
        assertEquals(1, cashRequestResult.cash().size());

        verify(mockCassetteHolder, times(1)).getAvailableCashBalance();
        verify(mockCassetteHolder, times(1)).getAvailableDenominations();
        verify(mockBillSelector, times(1)).selectBills(anyInt(), anyInt(), any(), any());
        verify(mockBillDispenser, times(1)).dispenseBills(denominationToQuantityMap);
    }

    @Test
    void loadCassettes() {

        when(mockCassetteHolder.loadCassette(isA(BillCassette.class))).thenReturn(true);
        doNothing().when(mockBillDispenser).readCellsInformation(anyList());

        var cassette = BillCassette.createCassette(ONE_HUNDRED, 1);

        atm.loadCassette(cassette);

        verify(mockCassetteHolder, times(1)).loadCassette(isA(BillCassette.class));
        verify(mockBillDispenser, times(1)).readCellsInformation(anyList());
    }

    @Test
    void extractCassette() {

        when(mockCassetteHolder.extractCassette(isA(BillCassette.class))).thenReturn(true);
        doNothing().when(mockBillDispenser).readCellsInformation(anyList());

        var cassette = BillCassette.createCassette(ONE_HUNDRED, 1);

        atm.extractCassette(cassette);

        verify(mockCassetteHolder, times(1)).extractCassette(isA(BillCassette.class));
        verify(mockBillDispenser, times(1)).readCellsInformation(anyList());
    }

    @Test
    void getBalance() {

        var availableBalance = 1000;
        when(mockCassetteHolder.getAvailableCashBalance()).thenReturn(availableBalance);

        int actualBalance = atm.getBalance();

        assertEquals(availableBalance, actualBalance);

        verify(mockCassetteHolder, times(1)).getAvailableCashBalance();
    }
}