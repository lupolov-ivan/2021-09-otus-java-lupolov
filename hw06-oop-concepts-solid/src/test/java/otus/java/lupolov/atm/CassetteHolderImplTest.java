package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Denomination;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static otus.java.lupolov.model.Denomination.ONE_HUNDRED;
import static otus.java.lupolov.model.Denomination.ONE_THOUSAND;

class CassetteHolderImplTest {

    CassetteHolder cassetteHolder;

    @BeforeEach
    void setUp() {
        cassetteHolder = new CassetteHolderImpl();
    }

    @Test
    void loadCassette_shouldBeReturnTrue() {

        var cassette = new BillCassette(ONE_THOUSAND, 10);

        assertTrue(cassetteHolder.loadCassette(cassette));
    }

    @Test
    void extractCassette_shouldBeReturnFalse() {
        var cassette = new BillCassette(ONE_THOUSAND, 10);

        assertFalse(cassetteHolder.extractCassette(cassette));
    }

    @Test
    void getAvailableDenominations() {

        var cassette1 = new BillCassette(ONE_THOUSAND, 0);
        var cassette2 = new BillCassette(ONE_HUNDRED, 10);

        cassetteHolder.loadCassette(cassette1);
        cassetteHolder.loadCassette(cassette2);

        Set<Denomination> availableDenominations = cassetteHolder.getAvailableDenominations();

        assertEquals(Set.of(ONE_HUNDRED), availableDenominations);
    }

    @Test
    void getAllToCassetteByDenomination() {

        var cassette1 = new BillCassette(ONE_THOUSAND, 10);
        var cassette2 = new BillCassette(ONE_THOUSAND, 0);

        cassetteHolder.loadCassette(cassette1);
        cassetteHolder.loadCassette(cassette2);


        List<BillCassette> actualCassettes = cassetteHolder.getAllToCassetteByDenomination(ONE_THOUSAND);

        assertEquals(List.of(cassette1), actualCassettes);
    }

    @Test
    void getAvailableCashBalance() {

        var cassette1 = new BillCassette(ONE_THOUSAND, 10);
        var cassette2 = new BillCassette(ONE_HUNDRED, 10);

        cassetteHolder.loadCassette(cassette1);
        cassetteHolder.loadCassette(cassette2);

        int availableCashBalance = cassetteHolder.getAvailableCashBalance();
        int expectedCashBalance = 11000;

        assertEquals(expectedCashBalance, availableCashBalance);
    }

    @Test
    void getBillBalanceByDenomination() {

        var expectedBillBalance = 10;
        var cassette = new BillCassette(ONE_THOUSAND, expectedBillBalance);

        cassetteHolder.loadCassette(cassette);

        int availableBillBalance = cassetteHolder.getBillBalanceByDenomination(ONE_THOUSAND);

        assertEquals(expectedBillBalance, availableBillBalance);
    }
}