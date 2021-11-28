package otus.java.lupolov;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toMap;

public class CellHandler {

    private final Map<BillDenomination, Cell> cells;

    public CellHandler(List<Cell> cells) {
        Objects.requireNonNull(cells);
        this.cells = cells.stream()
                .collect(toMap(Cell::getDenomination, Function.identity()));
    }

    public Set<BillDenomination> getAvailableDenominations() {
        return cells.keySet();
    }

    public Cell getAvailableCellWithMaxDenomination() {

        return cells.values()
                .stream()
                .filter(cell -> cell.getBalance() > 0)
                .max(comparing(cell -> cell.getDenomination().value()))
                .orElseThrow();
    }

    public Cell getAvailableCellWithMixDenomination() {

        return cells.values()
                .stream()
                .filter(cell -> cell.getBalance() > 0)
                .min(comparing(cell -> cell.getDenomination().value()))
                .orElseThrow();
    }

    public List<Bill> getBillsByDenomination(BillDenomination denomination, int quantity) {
        return cells.get(denomination).takeOutBills(quantity);
    }

    public int getAvailableBillsBalanceByDenomination(BillDenomination denomination) {
        return cells.get(denomination).getBalance();
    }

    public int getAvailableTotalSum() {

        return cells.values()
                .stream()
                .map(cell -> cell.getBalance() * cell.getDenomination().value())
                .reduce(0, Integer::sum);
    }
}
