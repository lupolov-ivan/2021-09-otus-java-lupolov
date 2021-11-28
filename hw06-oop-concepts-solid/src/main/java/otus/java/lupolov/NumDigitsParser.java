package otus.java.lupolov;

public class NumDigitsParser {

    public int parseSum(int sum, BillDenomination denomination) {

        if (sum <= 0 || denomination == null) {
            throw new IllegalArgumentException("Sum must be greater than zero and denomination must not be null");
        }

        return sum / denomination.value();
    }
}
