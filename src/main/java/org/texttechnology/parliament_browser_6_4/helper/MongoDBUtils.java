package org.texttechnology.parliament_browser_6_4.helper;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MongoDBUtils {

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static String host;
    private static int port;
    private static String username;
    private static String password;
    private static String databaseName;

    static {
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream = MongoDBUtils.class.getClassLoader().getResourceAsStream("properties/mongodb.properties");
            properties.load(resourceAsStream);

            host = properties.getProperty("remote_host");
            port = Integer.parseInt(properties.getProperty("remote_port"));
            username = properties.getProperty("remote_user");
            password = properties.getProperty("remote_password");
            databaseName = properties.getProperty("remote_database");
            connect();
        } catch (Exception e) {
            System.out.println("获取mongodb初始化配置失败");
            e.printStackTrace();
        }
    }

    /**
     * 连接到指定数据库
     * @throws IllegalArgumentException 如果数据库名称为空或null
     */
    private static void connect() {
        MongoCredential credential = MongoCredential.createCredential(username, databaseName, password.toCharArray());

        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(20)
                .socketTimeout(300000)
                .maxWaitTime(300000)
                .socketKeepAlive(true)
                .serverSelectionTimeout(300000)
                .connectTimeout(300000)
                .sslEnabled(false)
                .build();
        mongoClient = new MongoClient(new ServerAddress(host, port), options);

        mongoClient = new MongoClient(new ServerAddress(host, port), Collections.singletonList(credential), options);
        database = mongoClient.getDatabase(databaseName);
        System.out.println("Connect to database successfully!");
        System.out.println("MongoDatabase info is : " + database.getName());
    }

    /**
     * 获取指定名称的集合
     * @param collectionName 要获取的集合名称
     * @return 返回指定名称的集合
     */
    public static MongoCollection<Document> getCollection(String collectionName) {
        for (String existingCollection : database.listCollectionNames()) {
            if (existingCollection.equals(collectionName)) {
                return database.getCollection(collectionName); // 返回已存在的集合
            }
        }

        database.createCollection(collectionName);
        return database.getCollection(collectionName); // 返回新创建的集合
    }


    /**
     * 获取数据库中所有集合的名称
     * @return 返回包含所有集合名称的列表
     */
    public static List<String> listCollections() {
        List<String> collectionNames = new ArrayList<>();
        for (String name : database.listCollectionNames()) {
            collectionNames.add(name);
        }
        return collectionNames;
    }

    /**
     * 插入文档到MongoDB集合中
     * @param collection MongoDB集合
     * @param document 要插入的文档
     */
    public static void insertDocument(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
        System.out.println("Document insert successfully!");
    }

}
