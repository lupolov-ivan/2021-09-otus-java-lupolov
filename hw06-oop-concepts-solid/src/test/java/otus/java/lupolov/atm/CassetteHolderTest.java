package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Denomination;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static otus.java.lupolov.model.Denomination.*;

class CassetteHolderTest {

    static final int CELLS_QUANTITY = 3;

    CassetteHolder cassetteHolder;

    @BeforeEach
    void setUp() {
        cassetteHolder = new CassetteHolder(CELLS_QUANTITY);
    }

    @Test
    void loadCassette() {

        var cassette = BillCassette.createCassette(ONE_THOUSAND, 1);

        assertTrue(cassetteHolder.loadCassette(cassette));
        assertTrue(cassetteHolder.loadCassette(cassette));
        assertTrue(cassetteHolder.loadCassette(cassette));
        assertFalse(cassetteHolder.loadCassette(cassette));
    }

    @Test
    void extractCassette() {

        var cassette = BillCassette.createCassette(ONE_THOUSAND, 1);

        assertTrue(cassetteHolder.loadCassette(cassette));
        assertTrue(cassetteHolder.extractCassette(cassette));
        assertFalse(cassetteHolder.extractCassette(cassette));
    }

    @Test
    void getAvailableDenominations() {

        var cassette1 = BillCassette.createCassette(ONE_THOUSAND, 0);
        var cassette2 = BillCassette.createCassette(FIVE_HUNDRED, 1);
        var cassette3 = BillCassette.createCassette(TWO_HUNDRED, 0);

        List<BillCassette> billCassettes = List.of(cassette1, cassette2, cassette3);
        billCassettes.forEach(cassetteHolder::loadCassette);

        Set<Denomination> availableDenominations = cassetteHolder.getAvailableDenominations();

        assertEquals(1, availableDenominations.size());
        assertTrue(availableDenominations.contains(FIVE_HUNDRED));
    }

    @Test
    void getAvailableCashBalance() {

        var cassette1 = BillCassette.createCassette(ONE_THOUSAND, 1);
        var cassette2 = BillCassette.createCassette(FIVE_HUNDRED, 1);
        var cassette3 = BillCassette.createCassette(TWO_HUNDRED, 1);

        List<BillCassette> billCassettes = List.of(cassette1, cassette2, cassette3);
        billCassettes.forEach(cassetteHolder::loadCassette);

        int availableCashBalance = cassetteHolder.getAvailableCashBalance();
        int expectedCashBalance = 1700;

        assertEquals(expectedCashBalance, availableCashBalance);
    }

    @Test
    void getCassettes() {

        var cassette1 = BillCassette.createCassette(ONE_THOUSAND, 1);
        var cassette2 = BillCassette.createCassette(FIVE_HUNDRED, 1);
        var cassette3 = BillCassette.createCassette(TWO_HUNDRED, 1);

        List<BillCassette> billCassettes = List.of(cassette1, cassette2, cassette3);
        billCassettes.forEach(cassetteHolder::loadCassette);

        List<BillCassette> actualCassettes = cassetteHolder.getCassettes();

        assertEquals(billCassettes, actualCassettes);
    }
}