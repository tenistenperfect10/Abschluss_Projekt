package org.texttechnology.parliament_browser_6_4.controller;

import freemarker.template.Configuration;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnology.parliament_browser_6_4.dao.LogDAO;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.post;
/**
 * The {@code LogController} class is responsible for handling log-related routes in the application.
 * It defines the endpoint for retrieving logs and returning them in a JSON format. This controller
 * utilizes a {@code LogDAO} for data access and a {@code Configuration} instance for any potential
 * template rendering.
 */

public class LogController {

    private final LogDAO logDAO;
    private final Configuration cfg;

    /**
     * Constructs a new {@code LogController} with specified {@code LogDAO} and {@code Configuration}.
     * It initializes the routes for log-related requests.
     *
     * @param logDAO The data access object for logs.
     * @param cfg The FreeMarker configuration for rendering templates.
     * @throws IOException If an I/O error occurs during route initialization.
     */
    public LogController(LogDAO logDAO, Configuration cfg)
            throws IOException {
        this.logDAO = logDAO;
        this.cfg = cfg;
        initializeRoutes();
    }
    /**
     * Initializes the web routes for log-related operations. Specifically, it sets up an endpoint
     * for posting log data which returns the logs in a JSON format.
     *
     * @throws IOException If there is an error setting up the routes.
     */
    private void initializeRoutes() throws IOException {

        post("/logs", (request, response) -> {

            //  Build response data (using a simple example here)
            List<Document> logList = logDAO.getLogs();

            // Set response header
            response.header("Content-Type", "application/json");

            String json = convertListToJson(logList);
            // Return response data
            return json;
        });
    }
    /**
     * Converts a list of {@code Document} objects to a JSON string. It ensures the preservation of
     * insertion order of keys using {@code LinkedHashMap} and handles the conversion of {@code ObjectId}
     * to a string format.
     *
     * @param documents The list of {@code Document} objects to convert.
     * @return A JSON string representation of the documents list.
     */
    private static String convertListToJson(List<Document> documents) {
        List<String> jsonDocuments = new ArrayList<>();
        Document firstDocument = documents.get(0);
        List<String> fieldNames = new ArrayList<>(firstDocument.keySet());
        List<String> quotedFieldNames = new ArrayList<>();

        for (String fieldName : fieldNames) {
            quotedFieldNames.add("\"" + fieldName + "\"");
        }

        for (Document document : documents) {
            // Use LinkedHashMap to maintain the order of key-value pairs
            Map<String, Object> orderedMap = new LinkedHashMap<>(document);

            // Handle the _id field by converting ObjectId to its string representation
            ObjectId objectId = (ObjectId) orderedMap.get("_id");
            orderedMap.put("_id", objectId.toString());

            // Convert Map to JSON string
            String jsonDocument = new Document(orderedMap).toJson();
            jsonDocuments.add(jsonDocument);
        }

        String header = String.join(",", quotedFieldNames);
        String data = String.join(",", jsonDocuments);

        String json = "{\"header\":[" + header + "], \"data\":" + "[" + data + "]" + "}";

        return json;
    }
}
