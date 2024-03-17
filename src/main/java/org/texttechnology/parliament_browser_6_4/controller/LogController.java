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

            // 构建响应数据（这里使用一个简单的示例）
            List<Document> logList = logDAO.getLogs();

            // 设置响应头
            response.header("Content-Type", "application/json");

            String json = convertListToJson(logList);
            // 返回响应数据
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
            // 使用 LinkedHashMap 保持键值对的插入顺序
            Map<String, Object> orderedMap = new LinkedHashMap<>(document);

            // 处理 _id 字段，将 ObjectId 转换为字符串形式
            ObjectId objectId = (ObjectId) orderedMap.get("_id");
            orderedMap.put("_id", objectId.toString());

            // 将 Map 转换为 JSON 字符串
            String jsonDocument = new Document(orderedMap).toJson();
            jsonDocuments.add(jsonDocument);
        }

        String header = String.join(",", quotedFieldNames);
        String data = String.join(",", jsonDocuments);

        String json = "{\"header\":[" + header + "], \"data\":" + "[" + data + "]" + "}";

        return json;
    }
}
