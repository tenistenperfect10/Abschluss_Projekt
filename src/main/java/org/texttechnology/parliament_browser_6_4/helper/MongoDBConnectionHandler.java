package org.texttechnology.parliament_browser_6_4.helper;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
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

    public Document insertSpeech(Speech pSpeech) {

        Document mongoDocument = new Document();
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

        return mongoDocument;

    }

    public Document insertComment(Comment pComment){

        // creating a empty MongoDocument and add attributes
        Document mongoDocument = new Document();
        mongoDocument.put("_id", pComment.getID());
        mongoDocument.put("text", pComment.getContent());
        mongoDocument.put("speaker", pComment.getSpeaker()!=null ? pComment.getSpeaker().getID() : "");
        mongoDocument.put("speech", pComment.getSpeech()!=null ? pComment.getSpeech().getID() : "");

        this.getCollection("comment").insertOne(mongoDocument);

        return mongoDocument;
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
     * @param field 需要匹配的字段
     * @param value 字段匹配的值
     * @param updateField 需要更新的字段
     * @param updateValue 更新的值
     */
    public static void updateDocument(MongoCollection<Document> collection, String field, String value, String updateField, Object updateValue) {
        collection.updateMany(eq(field, value), Updates.set(updateField, updateValue));
        System.out.println("Document update successfully...");
    }

    /**
     * 删除指定集合中符合条件的文档
     * @param collection 要操作的MongoDB集合
     * @param field 字段名
     * @param value 字段值
     */
    public static void deleteDocument(MongoCollection<Document> collection, String field, String value) {
        collection.deleteOne(eq(field, value));
        System.out.println("Document delete successfully...");
    }

    /**
     * 删除指定名称的集合
     * @param collectionName 需要删除的集合名称
     */
    public void dropCollection(String collectionName) {
        MongoCollection<Document> collection = this.pDatabase.getCollection(collectionName);
        System.out.println("chose collection : " + collection.getNamespace());
        collection.drop();
        System.out.println("drop collection : " + collection.getNamespace());
        listCollections();
    }

    /**
     * 关闭数据库连接
     */
    public  void closeConnection() {
        pClient.close();
        System.out.println("Connection closed.");
    }




    /**
     * 通过字段和查询值查询文档条目
     * @param collection MongoDB文档集合
     * @param field 查询字段
     * @param queryValue 查询值
     * @return 包含查询结果的文档列表
     */
    public static List<Document> queryEntriesByField(MongoCollection<Document> collection, String field, String queryValue) {
        Document query = new Document(field, new Document("$eq", queryValue));
        List<Document> list = collection.find(query).into(new ArrayList<>());
        return list;
    }

    /**
     *
     * @param collection 操作的collection
     * @param fieldName 待更新文档的查询字段
     * @param fieldValue 待更新文档查询字段的值
     * @param json 待更新的json
     */
    public static void updateDocumentWithJson(MongoCollection<Document> collection, String fieldName, String fieldValue, String json) {

        // 要更新的document的查询条件
        Document query = new Document(fieldName, fieldValue); // 替换成你实际的查询条件

        // 要添加的字段和值
        Document update = new Document("$set", Document.parse(json));

        // 执行更新操作
        collection.updateOne(query, update);
    }

    public static void updateDocument(MongoCollection collection, String query, Document document){
        collection.updateOne(eq("_id", query), new Document("$set", document));
    }

}
