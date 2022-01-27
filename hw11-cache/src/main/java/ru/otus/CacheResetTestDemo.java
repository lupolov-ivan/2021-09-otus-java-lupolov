package ru.otus;

import lombok.extern.slf4j.Slf4j;
import ru.otus.cachehw.MyCache;
import ru.otus.cachehw.wrapper.ClientCacheWrapper;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.dbmirations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.mapper.DataTemplateJdbc;
import ru.otus.mapper.metadata.clazz.ClientEntityClassMetaData;
import ru.otus.mapper.metadata.sql.ClientEntitySqlMetaData;

import java.util.ArrayList;

/* For the test limited the amount of memory 32 MB*/
@Slf4j
public class CacheResetTestDemo {

    public static final String CLIENT_NAME_TEMPLATE = "Client-%s";

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pwd";

    public static void main(String[] args) {

        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        var migrationsExecutor = new MigrationsExecutorFlyway(dataSource);
        migrationsExecutor.executeMigrations();

        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        var clientEntityClassMetaData = new ClientEntityClassMetaData();
        var entitySQLMetaDataClient = new ClientEntitySqlMetaData();
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, clientEntityClassMetaData);
        var cacheWrapper = new ClientCacheWrapper(new MyCache<>());
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cacheWrapper);

        var clientIds = new ArrayList<Long>();

        for (int i = 1; i <= 1000; i++) {
            Client saveClient = dbServiceClient.saveClient(new Client(null,String.format(CLIENT_NAME_TEMPLATE, i)));
            Long clientId = saveClient.getId();
            clientIds.add(clientId);
            dbServiceClient.getClient(clientId);
        }

        clientIds.forEach(dbServiceClient::getClient);
    }
}