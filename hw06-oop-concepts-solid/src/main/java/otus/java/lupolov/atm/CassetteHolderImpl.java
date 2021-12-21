package otus.java.lupolov.atm;

import otus.java.lupolov.model.Denomination;

import java.util.*;
import java.util.stream.Collectors;

public class CassetteHolderImpl implements CassetteHolder {

    private final List<BillCassette> cassettes = new ArrayList<>();

    @Override
    public boolean loadCassette(BillCassette cassette) {
        return cassettes.add(cassette);
    }

    @Override
    public boolean extractCassette(BillCassette cassette) {
        return cassettes.remove(cassette);
    }

    @Override
    public Set<Denomination> getAvailableDenominations() {

        return cassettes.stream()
                .filter(cell -> cell.getBalance() > 0)
                .map(BillCassette::getDenomination)
                .collect(Collectors.toSet());
    }

    @Override
    public List<BillCassette> getAllToCassetteByDenomination(Denomination denomination) {

        return cassettes.stream()
                .filter(cassette -> cassette.getDenomination() == denomination)
                .filter(cassette -> cassette.getBalance() != 0)
                .toList();
    }

    @Override
    public int getAvailableCashBalance() {
        return cassettes.stream()
                .map(cell -> cell.getBalance() * cell.getDenomination().value())
                .reduce(0, Integer::sum);
    }

    @Override
    public int getBillBalanceByDenomination(Denomination denomination) {
        return cassettes.stream()
                .filter(cassette -> cassette.getDenomination() == denomination)
                .map(BillCassette::getBalance)
                .reduce(0, Integer::sum);
    }
}
