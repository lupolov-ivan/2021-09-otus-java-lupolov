package otus.java.lupolov.atm;

import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

import java.util.*;

public class BillDispenser {

    private final Map<Denomination, List<BillCassette>> denominationToCassetteMap = new HashMap<>();

    public void readCellsInformation(List<BillCassette> cassettes) {

        denominationToCassetteMap.clear();

        for (BillCassette cassette : cassettes) {

            Denomination key = cassette.getDenomination();
            denominationToCassetteMap.putIfAbsent(key, new ArrayList<>());

            denominationToCassetteMap.merge(key, List.of(cassette), (existing, newCassettes) -> {
                List<BillCassette> previousCells = new ArrayList<>(existing);
                previousCells.addAll(newCassettes);
                return previousCells;
            });
        }
    }

    public List<Bill> dispenseBills(Map<Denomination, Integer> denominationToQuantityMap) {

        return denominationToQuantityMap.entrySet()
                .stream()
                .map(this::dispenseBillsByDenomination)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Bill> dispenseBillsByDenomination(Map.Entry<Denomination, Integer> entry) {

        Denomination denomination = entry.getKey();
        Integer quantity = entry.getValue();

        List<BillCassette> notEmptyCassettes = denominationToCassetteMap.get(denomination)
                .stream()
                .filter(cassette -> cassette.getBalance() > 0)
                .toList();

        List<Bill> bills = new ArrayList<>();

        for (BillCassette cassette : notEmptyCassettes) {

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

    public int getDenominationBalance(Denomination denomination) {

        return denominationToCassetteMap.get(denomination)
                .stream()
                .map(BillCassette::getBalance)
                .reduce(0, Integer::sum);
    }

    public Map<Denomination, List<BillCassette>> getDenominationToCassetteMap() {
        return Map.copyOf(denominationToCassetteMap);
    }
}
