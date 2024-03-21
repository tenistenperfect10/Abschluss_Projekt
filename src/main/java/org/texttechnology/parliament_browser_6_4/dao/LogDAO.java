package org.texttechnology.parliament_browser_6_4.dao;

import cn.hutool.core.date.DateUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;


/**
 * The {@code LogDAO} class is a Data Access Object (DAO) responsible for managing log entries
 * in a MongoDB collection. It provides functionality to add new log entries and to retrieve existing log entries.
 * This class utilizes a singleton pattern to ensure that only one instance manages the log entries throughout the application.
 *
 * @author He Liu
 * @author Yu Ming
 */
public class LogDAO {
    private final MongoCollection<Document> logsCollection;

    public LogDAO(MongoDatabase logsCollection) {
        this.logsCollection = logsCollection.getCollection("logs");
    }

    private volatile static LogDAO instance;

    /**
     * run the log
     * @param database
     * @return
     */
    public static LogDAO init(MongoDatabase database) {
        if (instance == null) {
            synchronized (LogDAO.class) {
                if (instance == null) {
                    instance = new LogDAO(database);
                }
            }
        }
        return instance;
    }

    /**
     * A volatile instance of LogDAO to ensure thread-safe singleton instantiation.
     */
    public static LogDAO getInstance() {
        return instance;
    }


    /**
     * Adds a log entry to the logs collection. Each log entry contains information
     * such as the type of operation, the path, URL, method, request body, IP address of the requester,
     * and the timestamp of when the log was created.
     *
     * @param type The type of operation performed.
     * @param path The path accessed in the operation.
     * @param url The full URL accessed.
     * @param method The HTTP method used.
     * @param body The body of the request.
     * @param ip The IP address of the requester.
     */
    public void addLog(String type, String path, String url, String method,  String body, String ip) {
        Document post = new Document("path", path).append("url", url).append("type", type).append("method", method)
                .append("body", body).append("ip", ip).append("date", DateUtil.now());
        logsCollection.insertOne(post);
    }

    /**
     * Retrieves the latest 20 log entries from the logs collection, sorted in descending order by the document ID.
     * This method projects specific fields from the log entries to be included in the result set.
     *
     * @return A list of documents representing the latest 20 log entries.
     */
    public List<Document> getLogs() {
        return logsCollection.find().projection(new Document("fieldName1", 1)
                .append("_id", 1)
                .append("path", 1)
                .append("url", 1)
                .append("type", 1)
                .append("method", 1)
                .append("urlParams", 1)
                .append("ip", 1)
                .append("date", 1))
                .sort(descending("_id")).limit(20).into(new ArrayList<>());
    }
}
