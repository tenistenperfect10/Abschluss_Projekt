package org.texttechnology.parliament_browser_6_4.helper;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
/**
 * Handles connections to MongoDB, including initializing the connection with specified configuration,
 * accessing collections, and fetching documents from the database.
 */
public class MongoDBConnectionHandler {

    private MongoDBConfig pConfig = null;

    /**
     *  The connection with the MongoDB
     */
    private MongoClient pClient = null;

    /**
     * The object for the selected Database
     */
    private MongoDatabase pDatabase = null;

    /**
     * Amount of all Collections
     */
    private MongoCollection<Document> pCollection = null;

    /**
     * Constructs a new {@code MongoDBConnectionHandler} with the specified configuration,
     * and initializes the connection to MongoDB.
     *
     * @param pConfig The MongoDB configuration to use for connecting to the database.
     */
    public MongoDBConnectionHandler(MongoDBConfig pConfig){
        this.pConfig = pConfig;
        init();
    }

    /**
     * Initializes the connection to MongoDB using the provided configuration.
     * This includes setting up credentials, server address, connection options,
     * and selecting the default collection.
     */
    private void init(){

        // defind credentials (Username, database, password)
        MongoCredential credential = MongoCredential.createScramSha1Credential(pConfig.getMongoUsername(), pConfig.getMongoDatabase(), pConfig.getMongoPassword().toCharArray());
        // defining Hostname and Port
        ServerAddress seed = new ServerAddress(pConfig.getMongoHostname(), pConfig.getMongoPort());
        List<ServerAddress> seeds = new ArrayList(0);
        seeds.add(seed);
        // defining some Options
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(20)
                .socketTimeout(300000)
                .maxWaitTime(300000)
                .socketKeepAlive(true)
                .serverSelectionTimeout(300000)
                .connectTimeout(300000)
                .sslEnabled(false)
                .build();

        // connect to MongoDB
        pClient = new MongoClient(seeds, credential, options);

        // select database
        pDatabase = pClient.getDatabase(pConfig.getMongoDatabase());

        // select default connection
        pCollection = pDatabase.getCollection(pConfig.getMongoCollection());

        // some debug information
        System.out.println("Connect to "+pConfig.getMongoDatabase()+" on "+pConfig.getMongoHostname());

    }

    /**
     * Returns the default collection object.
     *
     * @return The default {@link MongoCollection}.
     */
    public MongoCollection getCollection(){
        return this.pCollection;
    }

    /**
     * Returns a collection object for the specified collection name.
     *
     * @param sCollection The name of the collection to retrieve.
     * @return The specified {@link MongoCollection}.
     */
    public MongoCollection getCollection(String sCollection){
        return this.pDatabase.getCollection(sCollection);
    }


    /**
     * Retrieves a document object by its ID from the specified collection.
     *
     * @param sID The ID of the document to retrieve.
     * @param sCollection The name of the collection to search in.
     * @return The found {@link Document}, or {@code null} if no document matches the given ID.
     */
    public Document getObject(String sID, String sCollection){

        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("_id", sID);

        FindIterable<Document> result = this.getCollection(sCollection).find(whereQuery);

        Document doc = null;

        MongoCursor<Document> it = result.iterator();

        while(it.hasNext()){
            doc = it.next();
        }

        return doc;

    }

    /**
     * Returns the database object for the currently selected MongoDB database.
     *
     * @return The current {@link MongoDatabase}.
     */
    public MongoDatabase getDatabase(){ return this.pDatabase;}

}
