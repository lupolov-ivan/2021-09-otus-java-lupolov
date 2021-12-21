package otus.java.lupolov.atm;

import otus.java.lupolov.model.Bill;
import otus.java.lupolov.model.Denomination;

import java.util.List;
import java.util.Map;

public interface BillDispenser {

    List<Bill> dispense(Map<Denomination, Integer> denominationToQuantityMap);
}
