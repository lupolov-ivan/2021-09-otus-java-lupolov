package otus.java.lupolov;

import java.util.Arrays;

public enum BillDenomination {

    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000);

    private final int value;

    BillDenomination(int value) {
        this.value = value;
    }

    public BillDenomination valueOf(int denominationValue) {
        return Arrays.stream(BillDenomination.values())
                .filter(denomination -> denomination.value == denominationValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Denomination with value not exist"));
    }

    public int value() {
        return value;
    }
}
