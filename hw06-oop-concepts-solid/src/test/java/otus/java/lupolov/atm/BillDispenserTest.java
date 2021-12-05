package otus.java.lupolov.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static otus.java.lupolov.model.Denomination.*;

class BillDispenserTest {


    BillDispenser billDispenser;

    @BeforeEach
    void setUp() {
        billDispenser = new BillDispenser();
    }

    @Test
    void readCellsInformation() {

        var cassette1 = BillCassette.createCassette(ONE_THOUSAND, 5);
        var cassette2 = BillCassette.createCassette(FIVE_HUNDRED, 5);
        var cassette3 = BillCassette.createCassette(TWO_HUNDRED, 5);
        var cassette4 = BillCassette.createCassette(TWO_HUNDRED, 5);
        var cassette5 = BillCassette.createCassette(FIFTY, 10);
        var cassette6 = BillCassette.createCassette(FIFTY, 10);

        List<BillCassette> cassettes = List.of(cassette1, cassette2, cassette3, cassette4, cassette5, cassette6);

        billDispenser.readCellsInformation(cassettes);

        Map<Denomination, List<BillCassette>> denominationToCassetteMap = billDispenser.getDenominationToCassetteMap();

        assertEquals(4, denominationToCassetteMap.size());
        assertEquals(1, denominationToCassetteMap.get(ONE_THOUSAND).size());
        assertEquals(1, denominationToCassetteMap.get(FIVE_HUNDRED).size());
        assertEquals(2, denominationToCassetteMap.get(TWO_HUNDRED).size());
        assertEquals(2, denominationToCassetteMap.get(FIFTY).size());
    }

    @Test
    void dispenseBills() {

        var cassette1 = BillCassette.createCassette(TWO_HUNDRED, 5);
        var cassette2 = BillCassette.createCassette(TWO_HUNDRED, 5);
        var cassette3 = BillCassette.createCassette(FIFTY, 10);
        var cassette4 = BillCassette.createCassette(FIFTY, 10);

        List<BillCassette> cassettes = List.of(cassette1, cassette2, cassette3, cassette4);

        billDispenser.readCellsInformation(cassettes);

        Map<Denomination, Integer> denominationToQuantityMap = Map.of(TWO_HUNDRED, 7, FIFTY, 5);

        List<Bill> bills = billDispenser.dispenseBills(denominationToQuantityMap);

        int expectedBillQuantity = 12;

        assertEquals(expectedBillQuantity, bills.size());
    }

    @Test
    void getDenominationBalance() {

        var cassette1 = BillCassette.createCassette(FIFTY, 10);
        var cassette2 = BillCassette.createCassette(FIFTY, 10);

        List<BillCassette> cassettes = List.of(cassette1, cassette2);
        billDispenser.readCellsInformation(cassettes);

        int actualBalance = billDispenser.getDenominationBalance(FIFTY);

        assertEquals(20, actualBalance);
    }
}