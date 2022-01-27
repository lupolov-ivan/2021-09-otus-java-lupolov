package ru.otus.crm.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractIntegrationTest {

    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }
}
