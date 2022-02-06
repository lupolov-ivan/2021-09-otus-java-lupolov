package ru.otus.core.sessionmanager;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class TransactionRunnerJdbc implements TransactionRunner {

    private final DataSource dataSource;

    @Override
    public <T> T doInTransaction(TransactionAction<T> action) {
        return wrapException(() -> {
            try (var connection = dataSource.getConnection()) {
                try {
                    var result = action.apply(connection);
                    connection.commit();
                    return result;
                } catch (SQLException ex) {
                    connection.rollback();
                    throw new DataBaseOperationException("doInTransaction exception", ex);
                }
            }
        });
    }

    private <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception ex) {
            throw new DataBaseOperationException("exception", ex);
        }
    }
}
