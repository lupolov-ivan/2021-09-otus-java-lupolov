package otus.java.lupolov.atm;

import otus.java.lupolov.model.Denomination;

import java.util.List;
import java.util.Set;

public interface CassetteHolder {

    boolean loadCassette(BillCassette cassette);

    boolean extractCassette(BillCassette cassette);

    Set<Denomination> getAvailableDenominations();

    List<BillCassette> getAllToCassetteByDenomination(Denomination denomination);

    int getAvailableCashBalance();

    int getBillBalanceByDenomination(Denomination denomination);
}
