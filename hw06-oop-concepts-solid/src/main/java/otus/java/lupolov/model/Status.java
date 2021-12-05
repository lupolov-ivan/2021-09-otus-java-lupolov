package otus.java.lupolov.model;

public enum Status {

    SUCCESS("Request success"),
    NO_MONEY("The ATM is out of money"),
    NOT_ENOUGH_MONEY("There is not enough money in the ATM"),
    NOT_MULTIPLE_MIN_DENOMINATION("The requested amount is not a multiple of the minimum denomination"),
    IMPOSSIBLE_ISSUE_AMOUNT_FROM_REMAINING_BILLS("It is impossible to issue the requested amount from the remaining bills");

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
