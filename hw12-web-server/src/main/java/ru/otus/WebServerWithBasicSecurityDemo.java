package ru.otus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.entity.Address;
import ru.otus.crm.entity.Client;
import ru.otus.crm.entity.Phone;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.model.User;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithBasicSecurity;
import ru.otus.services.InMemoryLoginServiceImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

import java.util.List;

import static ru.otus.model.Roles.*;

public class WebServerWithBasicSecurityDemo {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SessionFactory sessionFactory
                = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        var objectMapper = new ObjectMapper();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        UserDao userDao = new InMemoryUserDao()
                .withUser(new User(1L, "John Dou", "user", "1111", List.of(USER)))
                .withUser(new User(2L, "John Dou", "admin", "1111", List.of(ADMIN)));

        LoginService loginService = new InMemoryLoginServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(
                WEB_SERVER_PORT, objectMapper, templateProcessor, dbServiceClient, loginService);

        usersWebServer.start();
        usersWebServer.join();
    }
}
