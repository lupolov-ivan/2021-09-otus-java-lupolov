package otus.java.lupolov.model;

import java.util.List;

public record CashRequestResult(Status status, String infoMessage, List<Bill> cash) {
}
