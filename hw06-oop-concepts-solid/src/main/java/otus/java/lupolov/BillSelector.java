package otus.java.lupolov;

import java.util.ArrayList;
import java.util.List;


public class BillSelector {

    private final CellHandler cellHandler;
    private final NumDigitsParser digitsParser;

    public BillSelector(CellHandler cellHandler, NumDigitsParser digitsParser) {
        this.cellHandler = cellHandler;
        this.digitsParser = digitsParser;
    }

    public List<Bill> selectBillsBySum(int sum) {

        if (sum > cellHandler.getAvailableTotalSum()) {
            throw new IllegalArgumentException("Sum greater than total sum all cells");
        }

        int remainSum = sum;
        List<Bill> resultBills = new ArrayList<>();

        while (remainSum > 0) {

            Cell maxDenCell = cellHandler.getAvailableCellWithMaxDenomination();
            BillDenomination cellDenomination = maxDenCell.getDenomination();
            int requiredNumBillsByDenomination = sum / cellDenomination.value();

            int cellBillBalance = maxDenCell.getBalance();

            if (cellBillBalance > requiredNumBillsByDenomination) {
                List<Bill> bills = maxDenCell.takeOutBills(requiredNumBillsByDenomination);
                resultBills.addAll(bills);

                remainSum = remainSum - (requiredNumBillsByDenomination * cellDenomination.value());
            } else {
                List<Bill> bills = maxDenCell.takeOutBills(cellBillBalance);
                resultBills.addAll(bills);
                remainSum = remainSum - (cellBillBalance * cellDenomination.value());
            }
        }

        return resultBills;
    }
}
