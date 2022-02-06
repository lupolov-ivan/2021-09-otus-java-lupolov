package ru.otus.crm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.cachehw.wrapper.ClientCacheWrapper;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.base.AbstractIntegrationTest;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.dbmirations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Client;
import ru.otus.mapper.DataTemplateJdbc;
import ru.otus.mapper.metadata.clazz.ClientEntityClassMetaData;
import ru.otus.mapper.metadata.sql.ClientEntitySqlMetaData;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class DbServiceClientImplTest extends AbstractIntegrationTest {

    private DBServiceClient dbServiceClient;
    private HwCache<String, Client> cacheSpy;

    @BeforeEach
    public void setUp() {

        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var dataSource = new DriverManagerDataSource(dbUrl, dbUserName, dbPassword);
        var migrationsExecutor = new MigrationsExecutorFlyway(dataSource);
        migrationsExecutor.executeMigrations();

        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();
        var clientEntityClassMetaData = new ClientEntityClassMetaData();
        var entitySQLMetaDataClient = new ClientEntitySqlMetaData();
        var clientTemplate = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, clientEntityClassMetaData);

        cacheSpy = spy(new MyCache<>());
        var cacheWrapper = new ClientCacheWrapper(cacheSpy);
        dbServiceClient = new DbServiceClientImpl(transactionRunner, clientTemplate, cacheWrapper);
    }


    @Test
    void shouldGetClientFromCacheFasterThanDB() {

        var client = new Client(null,"John");
        var savedClient = dbServiceClient.saveClient(client);

        var clientId = savedClient.getId();

        long start = System.currentTimeMillis();
        dbServiceClient.getClient(clientId);
        long end = System.currentTimeMillis();

        long dbFetchTime = end - start;

        start = System.currentTimeMillis();
        dbServiceClient.getClient(clientId);
        end = System.currentTimeMillis();

        long cacheFetchTime = end - start;

        assertTrue(dbFetchTime > cacheFetchTime);
        verify(cacheSpy, times(2)).get(String.valueOf(clientId));
        verify(cacheSpy, times(1)).put(String.valueOf(clientId), savedClient);
    }
}