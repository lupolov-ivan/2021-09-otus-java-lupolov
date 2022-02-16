package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;

import static java.util.Collections.emptyMap;

public class ClientsFormServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients_form.html";


    private final TemplateProcessor templateProcessor;

    public ClientsFormServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, emptyMap()));
    }

}
