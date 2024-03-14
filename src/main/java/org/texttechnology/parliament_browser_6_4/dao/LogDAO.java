package org.texttechnology.parliament_browser_6_4.dao;

import cn.hutool.core.date.DateUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;

/**
 * The {@code LogDAO} class is responsible for data access operations related to logs in the MongoDB database.
 * It provides functionalities to add a new log entry and retrieve log entries. This class implements a singleton
 * pattern to ensure only one instance is used throughout the application.
 */
public class LogDAO {

    private final MongoCollection<Document> logsCollection;
    /**
     * Initializes a new instance of {@code LogDAO} with a reference to the logs collection in the MongoDB database.
     *
     * @param logsCollection The MongoDB database connection.
     */
    public LogDAO(MongoDatabase logsCollection) {
        this.logsCollection = logsCollection.getCollection("logs");
    }

    private volatile static LogDAO instance;
    /**
     * Initializes the singleton instance of {@code LogDAO} with the specified MongoDB database. If the instance
     * already exists, this method returns the existing instance.
     *
     * @param database The MongoDB database connection.
     * @return The singleton instance of {@code LogDAO}.
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
     * Retrieves the singleton instance of {@code LogDAO}.
     *
     * @return The singleton instance of {@code LogDAO}.
     */
    public static LogDAO getInstance() {
        return instance;
    }

    /**
     * Adds a new log entry to the logs collection in the MongoDB database.
     *
     * @param type   The type of the log entry.
     * @param path   The path associated with the log entry.
     * @param url    The URL associated with the log entry.
     * @param method The HTTP method used (e.g., GET, POST).
     * @param body   The body of the request.
     * @param ip     The IP address from which the request originated.
     */
    public void addLog(String type, String path, String url, String method,  String body, String ip) {
        Document post = new Document("path", path).append("url", url).append("type", type).append("method", method)
                .append("body", body).append("ip", ip).append("date", DateUtil.now());
        logsCollection.insertOne(post);
    }
    /**
     * Retrieves the most recent 20 log entries from the logs collection, sorted by their insertion order.
     *
     * @return A list of {@link Document} representing the log entries.
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
