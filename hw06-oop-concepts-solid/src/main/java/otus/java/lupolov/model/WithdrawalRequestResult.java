package otus.java.lupolov.model;

import java.util.Map;

public record WithdrawalRequestResult(Status status,
                                      String infoMessage,
                                      Map<Denomination, Integer> denominationToQuantityMap) {

}
