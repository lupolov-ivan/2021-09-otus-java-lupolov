package otus.java.lupolov.atm;

import otus.java.lupolov.model.Denomination;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CassetteHolder {

    private final int cellsQuantity;
    private final List<BillCassette> cassettes;

    public CassetteHolder(int cellsQuantity) {
        this.cellsQuantity = cellsQuantity;
        this.cassettes = new ArrayList<>(cellsQuantity);
    }

    public boolean loadCassette(BillCassette cassette) {

        if (cassettes.size() >= cellsQuantity) {
            return false;
        }

        return cassettes.add(cassette);
    }

    public boolean extractCassette(BillCassette cassette) {
        return cassettes.remove(cassette);
    }

    public Set<Denomination> getAvailableDenominations() {

        return cassettes.stream()
                .filter(cell -> cell.getBalance() > 0)
                .map(BillCassette::getDenomination)
                .collect(Collectors.toSet());
    }

    public int getAvailableCashBalance() {

        return cassettes.stream()
                .map(cell -> cell.getBalance() * cell.getDenomination().value())
                .reduce(0, Integer::sum);
    }

    public List<BillCassette> getCassettes() {
        return cassettes;
    }
}
