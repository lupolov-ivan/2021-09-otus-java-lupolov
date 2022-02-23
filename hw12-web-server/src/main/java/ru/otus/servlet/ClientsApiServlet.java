package ru.otus.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.otus.crm.entity.Client;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;

@Slf4j
public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;
    private static final String SERIALIZATION_ERR_VALUE = "N/A";

    private final ObjectMapper objectMapper;
    private final DBServiceClient dbServiceClient;

    public ClientsApiServlet(ObjectMapper objectMapper, DBServiceClient dbServiceClient) {
        this.objectMapper = objectMapper;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Client client = dbServiceClient.getClient(extractIdFromRequest(request)).orElse(null);

        String responseJson = toJson(client);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(responseJson);
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)
                ? path[ID_PATH_PARAM_POSITION]
                : String.valueOf(- 1);
        return Long.parseLong(id);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error during serialization", e);
        }
        return SERIALIZATION_ERR_VALUE;
    }
}
