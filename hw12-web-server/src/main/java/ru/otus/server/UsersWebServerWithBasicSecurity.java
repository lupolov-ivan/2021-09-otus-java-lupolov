package ru.otus.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.model.Roles;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.ClientsApiServlet;
import ru.otus.servlet.ClientsFormServlet;
import ru.otus.servlet.ClientsServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersWebServerWithBasicSecurity implements UsersWebServer {

    private static final String CONSTRAINT_NAME = "auth";
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final ObjectMapper objectMapper;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final DBServiceClient dbServiceClient;
    private final LoginService loginService;

    public UsersWebServerWithBasicSecurity(int port,
                                           ObjectMapper objectMapper,
                                           TemplateProcessor templateProcessor,
                                           DBServiceClient dbServiceClient,
                                           LoginService loginService) {
        this.server = new Server(port);
        this.objectMapper = objectMapper;
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
        this.loginService = loginService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        List<ConstraintMapping> constraintMappings = ConstraintConfigurer.configurer()
                .addConstraint(new Roles[] {Roles.USER, Roles.ADMIN}, HttpMethod.GET,"/clients", "/api/clients/*")
                .addConstraint(new Roles[] {Roles.ADMIN}, HttpMethod.GET, "/clients/add")
                .addConstraint(new Roles[] {Roles.ADMIN}, HttpMethod.POST, "/clients")
                .build();

        var handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, constraintMappings));

        server.setHandler(handlers);
        return server;
    }

    private ResourceHandler createResourceHandler() {
        var resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {

        var servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        var clientsServlet = new ClientsServlet(templateProcessor, dbServiceClient);
        var clientsFormServlet = new ClientsFormServlet(templateProcessor);
        var clientsApiServlet = new ClientsApiServlet(objectMapper, dbServiceClient);

        servletContextHandler.addServlet(new ServletHolder(clientsServlet), "/clients");
        servletContextHandler.addServlet(new ServletHolder(clientsFormServlet), "/clients/add");
        servletContextHandler.addServlet(new ServletHolder(clientsApiServlet), "/api/clients/*");

        return servletContextHandler;
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler, List<ConstraintMapping> constraintMappings) {

        var security = new ConstraintSecurityHandler();

        security.setAuthenticator(new BasicAuthenticator());
        security.setLoginService(loginService);
        security.setConstraintMappings(constraintMappings);
        security.setHandler(new HandlerList(servletContextHandler));

        return security;
    }

    private static class ConstraintConfigurer {

        private final List<ConstraintMapping> constraintMappings = new ArrayList<>();

        private ConstraintConfigurer() {}

        public static ConstraintConfigurer configurer() {
            return new ConstraintConfigurer();
        }

        public ConstraintConfigurer addConstraint(Roles[] roles, HttpMethod httpMethod, String... paths) {

            var rolesArray = Arrays.stream(roles)
                    .map(Roles::name)
                    .toArray(String[]::new);

            var constraint = new Constraint();
            constraint.setName(CONSTRAINT_NAME);
            constraint.setAuthenticate(true);
            constraint.setRoles(rolesArray);

            Arrays.stream(paths).forEachOrdered(path -> {
                var mapping = new ConstraintMapping();
                mapping.setPathSpec(path);
                mapping.setConstraint(constraint);
                mapping.setMethod(httpMethod.asString());
                constraintMappings.add(mapping);
            });

            return this;
        }

        public List<ConstraintMapping> build() {
            List<ConstraintMapping> copyConstraints = new ArrayList<>(constraintMappings);
            constraintMappings.clear();
            return copyConstraints;
        }
    }
}
