package otus.java.lupolov.model;

import java.util.Map;

public record SelectionResult(Status status, Map<Denomination, Integer> billsToTake) {
}
