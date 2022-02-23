package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.otus.crm.entity.Address;
import ru.otus.crm.entity.Client;
import ru.otus.crm.entity.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Slf4j
public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private static final String FORM_ATTR_NAME = "name";
    private static final String FORM_ATTR_STREET = "street";
    private static final String FORM_ATTR_PHONE = "phones";
    private static final int MAX_INACTIVE_INTERVAL = 30;

    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {

        List<Client> clients = dbServiceClient.findAll();

        var paramsMap = new HashMap<String, Object>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(FORM_ATTR_NAME);
        String street = request.getParameter(FORM_ATTR_STREET);
        String phoneNumbers = request.getParameter(FORM_ATTR_PHONE);

        var newClient = new Client();
        newClient.setName(name);

        if (nonNull(phoneNumbers)&& !phoneNumbers.isBlank()) {
            List<Phone> phones = Arrays.stream(phoneNumbers.split(","))
                    .map(phoneNumber -> new Phone(null, phoneNumber))
                    .collect(toList());
            newClient.setPhones(phones);
        }

        if(nonNull(street) && !street.isBlank()) {
            var address = new Address(null, street);
            newClient.setAddress(address);
        }

        Client client = dbServiceClient.saveClient(newClient);
        log.info("New client saved: {}", client);

        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
        response.sendRedirect("/clients");
    }
}
