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
/**
 * A utility class providing static methods for MongoDB interactions. It includes functionality
 * for connecting to a MongoDB database, accessing or creating collections, listing collection names,
 * and inserting documents into a collection. The class automatically establishes a connection
 * with the database upon loading, based on properties specified in a configuration file.
 * @author Yingzhu Chen
 * @author He Liu
 */
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
            System.out.println("Failure to get mongodb initialization configuration");
            e.printStackTrace();
        }
    }

    /**
     * Establishes a connection to the MongoDB database using the settings configured in the
     * `mongodb.properties` file. It initializes the {@code mongoClient} and {@code database} static fields.
     * This method is called automatically when the class is loaded.
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
     * Retrieves a collection from the MongoDB database by its name. If the collection does not exist,
     * it is created.
     * @param collectionName The name of the collection to retrieve.
     * @return The MongoCollection instance for the specified name.
     */
    public static MongoCollection<Document> getCollection(String collectionName) {
        for (String existingCollection : database.listCollectionNames()) {
            if (existingCollection.equals(collectionName)) {
                return database.getCollection(collectionName); // Returns a collection that already exists
            }
        }

        database.createCollection(collectionName);
        return database.getCollection(collectionName); // Returns the newly created collection
    }


    /**
     * Lists the names of all collections in the MongoDB database.
     * @return A list of collection names in the database.
     */
    public static List<String> listCollections() {
        List<String> collectionNames = new ArrayList<>();
        for (String name : database.listCollectionNames()) {
            collectionNames.add(name);
        }
        return collectionNames;
    }

    /**
     * Inserts a document into the specified MongoDB collection.
     * @param collection The collection into which the document will be inserted.
     * @param document The document to insert into the collection.
     */
    public static void insertDocument(MongoCollection<Document> collection, Document document) {
        collection.insertOne(document);
        System.out.println("Document insert successfully!");
    }

}
