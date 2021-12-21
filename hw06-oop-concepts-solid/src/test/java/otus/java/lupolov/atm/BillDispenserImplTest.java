package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static otus.java.lupolov.model.Denomination.ONE_HUNDRED;
import static otus.java.lupolov.model.Denomination.ONE_THOUSAND;

class BillDispenserImplTest {

    CassetteHolder cassetteHolderMock;
    BillDispenser billDispenser;

    @BeforeEach
    void setUp() {
        cassetteHolderMock = mock(CassetteHolder.class);
        billDispenser = new BillDispenserImpl(cassetteHolderMock);
    }

    @Test
    void dispense() {

        var cassette1 = new BillCassette(ONE_THOUSAND, 10);
        var cassette3 = new BillCassette(ONE_HUNDRED, 10);
        var cassette4 = new BillCassette(ONE_HUNDRED, 10);

        List<BillCassette> oneThousandList = List.of(cassette1);
        List<BillCassette> oneHundredList = List.of(cassette3, cassette4);

        when(cassetteHolderMock.getAllToCassetteByDenomination(ONE_THOUSAND)).thenReturn(oneThousandList);
        when(cassetteHolderMock.getAllToCassetteByDenomination(ONE_HUNDRED)).thenReturn(oneHundredList);

        Map<Denomination, Integer> requestedMap = Map.of(ONE_THOUSAND, 5, ONE_HUNDRED, 15);

        List<Bill> actualBills = billDispenser.dispense(requestedMap);

        assertEquals(20, actualBills.size());

        verify(cassetteHolderMock, times(1)).getAllToCassetteByDenomination(ONE_THOUSAND);
        verify(cassetteHolderMock, times(1)).getAllToCassetteByDenomination(ONE_HUNDRED);
    }
}