package ru.otus.crm.dbmirations;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

@Slf4j
public class MigrationsExecutorFlyway {

    private final Flyway flyway;

    public MigrationsExecutorFlyway(DataSource dataSource) {
        flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
    }

    public void executeMigrations() {
        log.info("db migration started...");
        flyway.clean();
        flyway.migrate();
        log.info("db migration finished.");
    }
}
