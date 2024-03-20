package org.texttechnology.parliament_browser_6_4.controller;

import freemarker.template.Configuration;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnology.parliament_browser_6_4.dao.LogDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.post;

/**
 *  set up and manage routes for handling requests related to log
 */
public class LogController {

    private final LogDAO logDAO;
    private final Configuration cfg;

    public LogController(LogDAO logDAO, Configuration cfg)
            throws IOException {
        this.logDAO = logDAO;
        this.cfg = cfg;
        initializeRoutes();
    }


    private void initializeRoutes() throws IOException {

        post("/logs", (request, response) -> {

            // Constructing response data (using a simple example here)
            List<Document> logList = logDAO.getLogs();

            // Setting the response header
            response.header("Content-Type", "application/json");

            String json = convertListToJson(logList);
            // Return response data
            return json;
        });
    }

    private static String convertListToJson(List<Document> documents) {
        List<String> jsonDocuments = new ArrayList<>();
        Document firstDocument = documents.get(0);
        List<String> fieldNames = new ArrayList<>(firstDocument.keySet());
        List<String> quotedFieldNames = new ArrayList<>();

        for (String fieldName : fieldNames) {
            quotedFieldNames.add("\"" + fieldName + "\"");
        }

        for (Document document : documents) {
            // Keeping Key-Value Pairs in Insertion Order with LinkedHashMap
            Map<String, Object> orderedMap = new LinkedHashMap<>(document);

            // Processes the _id field, converting the ObjectId to string form
            ObjectId objectId = (ObjectId) orderedMap.get("_id");
            orderedMap.put("_id", objectId.toString());

            // Converting a Map to a JSON String
            String jsonDocument = new Document(orderedMap).toJson();
            jsonDocuments.add(jsonDocument);
        }

        String header = String.join(",", quotedFieldNames);
        String data = String.join(",", jsonDocuments);

        String json = "{\"header\":[" + header + "], \"data\":" + "[" + data + "]" + "}";

        return json;
    }
}
