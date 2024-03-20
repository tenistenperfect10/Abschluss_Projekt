package org.texttechnology.parliament_browser_6_4.helper;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONException;
import org.json.JSONObject;
import org.texttechnology.parliament_browser_6_4.data.*;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static org.texttechnology.parliament_browser_6_4.helper.MongoDBUtils.listCollections;

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

    /**
     * insert speech to mongoDB
     * @param pSpeech
     * @return
     */
    public Document insertSpeech(Speech pSpeech) {
        Document rDocument = null;

        rDocument = getObject(pSpeech.getID(), "speech");

        if(rDocument==null){

            Document mongoDocument = null;
            try {
                mongoDocument.put("_id", pSpeech.getID());
                mongoDocument.put("text", pSpeech.getPlainText());
                mongoDocument.put("length", pSpeech.getPlainText().length());
                mongoDocument.put("speaker", pSpeech.getSpeaker().getID());

                BasicDBObject protocolObject = new BasicDBObject();
                PlenaryProtocol pProtocol = pSpeech.getProtocol();

                protocolObject.put("date", pProtocol.getDate().getTime());
                protocolObject.put("starttime", pProtocol.getStartTime().getTime());
                protocolObject.put("endtime", pProtocol.getEndTime().getTime());
                protocolObject.put("index", pProtocol.getIndex());
                protocolObject.put("title", pProtocol.getTitle());
                protocolObject.put("place", pProtocol.getPlace());
                protocolObject.put("wp", pProtocol.getWahlperiode());

                mongoDocument.put("protocol", protocolObject);

                AgendaItem pItem = pSpeech.getAgendaItem();

                JSONObject agendaItem = new JSONObject();
                agendaItem.put("id", pItem.getID());
                agendaItem.put("index", pItem.getIndex());
                agendaItem.put("title", pItem.getTitle());

                List comments = new ArrayList<>();

                for (Comment c : pSpeech.getComments()) {
                    comments.add(c.getID());
                }

                mongoDocument.put("comments", comments);
                mongoDocument.put("agenda", Document.parse(agendaItem.toString()));

                mongoDocument.put("speaker", pSpeech.getSpeaker().getID());

                this.getCollection("speech").insertOne(mongoDocument);

                rDocument = getObject(pSpeech.getID(), "speech");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }

        return rDocument;
    }

    /**
     * insert comment to mongoDB
     * @param pComment
     * @return
     */

    public Document insertComment(Comment pComment){

        Document rDocument = null;

        rDocument = getObject(pComment.getID(), "comment");

        if(rDocument==null){
            Document mongoDocument = null;

            // creating a empty MongoDocument and add attributes
            mongoDocument.put("_id", pComment.getID());
            mongoDocument.put("text", pComment.getContent());
            mongoDocument.put("speaker", pComment.getSpeaker()!=null ? pComment.getSpeaker().getID() : "");
            mongoDocument.put("speech", pComment.getSpeech()!=null ? pComment.getSpeech().getID() : "");


            this.getCollection("comment").insertOne(mongoDocument);
            rDocument = getObject(pComment.getID(), "comment");
        }


        return rDocument;
    }

    public Document insertSpeaker(Speaker speaker){

        Document mongoDocument = new Document();
        mongoDocument.put("_id", speaker.getID());
        mongoDocument.put("name", speaker.getName());
        mongoDocument.put("firstName", speaker.getFirstName());
        mongoDocument.put("title", speaker.getTitle());
        mongoDocument.put("geburtsdatum", speaker.getGeburtsdatum());
        mongoDocument.put("geburtsort", speaker.getGeburtsort());
        mongoDocument.put("sterbedatum", speaker.getSterbeDatum());
        mongoDocument.put("geschlecht", speaker.getGeschlecht().toString());
        mongoDocument.put("beruf", speaker.getBeruf());
        mongoDocument.put("akademischertitel", speaker.getAkademischerTitel());
        mongoDocument.put("familienstand", speaker.getFamilienstand());
        mongoDocument.put("religion", speaker.getReligion());
        mongoDocument.put("vita", speaker.getVita());
        mongoDocument.put("adressing", speaker.getAdressing());

        List<Integer> iAbsendes = new ArrayList<>();
        for (PlenaryProtocol absence : speaker.getAbsences()) {
            iAbsendes.add(absence.getIndex());
        }

        mongoDocument.put("absence", iAbsendes);
        if(speaker.getParty()!=null){
            mongoDocument.put("party", speaker.getParty().getName());
        }
        if(speaker.getFraction()!=null){
            mongoDocument.put("fraction", speaker.getFraction().getName());
        }
        mongoDocument.put("role", speaker.getRole());
        return mongoDocument;


    }

    /**
     * Enquiry Document
     * @param query
     * @param sCollection
     * @return
     */
    public MongoCursor queryDocuments(BasicDBObject query, String sCollection){
        FindIterable result = this.getCollection(sCollection).find(query);
        return result.iterator();
    }

    /**
     * Method to execute a query
     * @param query
     * @return
     */
    public MongoCursor queryDocuments(String query, String sCollection){
        return queryDocuments(BasicDBObject.parse(query), sCollection);
    }

    /**
     * Enquiry Document
     * @param collection MongoDB Documentation Collection
     * @param filter filtration conditions
     * @return List of documents containing query results
     */
    public static List<Document> findDocuments(MongoCollection<Document> collection, Bson filter) {
        List<Document> documents = new ArrayList<>();
        FindIterable<Document> result = collection.find(filter);
        // Iterate through the query results and output
        for (Document document : result) {
            documents.add(document);
        }
        return documents;
    }


    /**
     * Update Documentation
     * @param collection MongoDB Documentation Collection
     * @param field Fields to match
     * @param value Values matched by fields
     * @param updateField Fields to be updated
     * @param updateValue Updated values
     */
    public static void updateDocument(MongoCollection<Document> collection, String field, String value, String updateField, Object updateValue) {
        collection.updateMany(eq(field, value), Updates.set(updateField, updateValue));
        System.out.println("Document update successfully...");
    }

    /**
     * Deletes eligible documents in a specified collection
     * @param collection The MongoDB collection to manipulate
     * @param field field name
     * @param value field value
     */
    public static void deleteDocument(MongoCollection<Document> collection, String field, String value) {
        collection.deleteOne(eq(field, value));
        System.out.println("Document delete successfully...");
    }

    /**
     * Deletes a collection with a specified name
     * @param collectionName Name of the collection to be deleted
     */
    public void dropCollection(String collectionName) {
        MongoCollection<Document> collection = this.pDatabase.getCollection(collectionName);
        System.out.println("chose collection : " + collection.getNamespace());
        collection.drop();
        System.out.println("drop collection : " + collection.getNamespace());
        listCollections();
    }

    /**
     * Close the database connection
     */
    public  void closeConnection() {
        pClient.close();
        System.out.println("Connection closed.");
    }




    public static void updateDocument(MongoCollection collection, String query, Document document){
        collection.updateOne(eq("_id", query), new Document("$set", document));
    }

}
