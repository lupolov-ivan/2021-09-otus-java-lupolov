package otus.java.lupolov.atm;

import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

import java.util.*;


public class BillDispenserImpl implements BillDispenser {

    private final CassetteHolder cassetteHolder;

    public BillDispenserImpl(CassetteHolder cassetteHolder) {
        this.cassetteHolder = cassetteHolder;
    }

    @Override
    public List<Bill> dispense(Map<Denomination, Integer> denominationToQuantityMap) {

        return denominationToQuantityMap.entrySet()
                .stream()
                .map(entry -> dispenseBillsByDenomination(entry.getKey(), entry.getValue()))
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Bill> dispenseBillsByDenomination(Denomination denomination, Integer quantity) {

        List<Bill> bills = new ArrayList<>();

        for (BillCassette cassette : cassetteHolder.getAllToCassetteByDenomination(denomination)) {
            while (quantity != 0) {

                if (cassette.getBalance() == 0) {
                    break;
                }

                bills.add(cassette.extractBill());
                quantity--;
            }
        }

        return bills;
    }
}
