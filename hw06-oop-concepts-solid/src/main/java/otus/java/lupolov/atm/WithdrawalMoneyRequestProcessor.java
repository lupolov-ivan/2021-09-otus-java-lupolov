package otus.java.lupolov.atm;

import otus.java.lupolov.model.WithdrawalRequestResult;

public interface WithdrawalMoneyRequestProcessor {

    WithdrawalRequestResult processWithdrawalRequest(int sum);
}
