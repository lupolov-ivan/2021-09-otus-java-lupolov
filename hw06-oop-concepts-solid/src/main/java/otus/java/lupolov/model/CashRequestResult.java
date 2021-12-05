package otus.java.lupolov.model;

import java.util.List;

public record CashRequestResult(Status status, List<Bill> cash) {
}
