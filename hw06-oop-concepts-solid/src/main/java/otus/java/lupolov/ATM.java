package otus.java.lupolov;

import java.util.List;

public class ATM {

    private final BillSelector billSelector;

    public ATM(BillSelector billSelector) {
        this.billSelector = billSelector;
    }

    public void loadCells() {

    }

    public List<Bill> getCash(int sum) {
        return billSelector.selectBillsBySum(sum);
    }

    public int getBalance() {
        return 0;
    }
}
