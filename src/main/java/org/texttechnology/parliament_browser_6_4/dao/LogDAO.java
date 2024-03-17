package org.texttechnology.parliament_browser_6_4.dao;

import cn.hutool.core.date.DateUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Sorts.descending;

public class LogDAO {
    private final MongoCollection<Document> logsCollection;

    public LogDAO(MongoDatabase logsCollection) {
        this.logsCollection = logsCollection.getCollection("logs");
    }

    private volatile static LogDAO instance;

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

    public static LogDAO getInstance() {
        return instance;
    }

    public void addLog(String type, String path, String url, String method,  String body, String ip) {
        Document post = new Document("path", path).append("url", url).append("type", type).append("method", method)
                .append("body", body).append("ip", ip).append("date", DateUtil.now());
        logsCollection.insertOne(post);
    }

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
