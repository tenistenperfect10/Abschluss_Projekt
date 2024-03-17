package org.texttechnology.parliament_browser_6_4.data.Impl;

import cn.hutool.json.JSONUtil;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnology.parliament_browser_6_4.data.Comment;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.Speaker;
import org.texttechnology.parliament_browser_6_4.data.Speech;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConfig;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConnectionHandler;

import java.util.*;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Sorts.descending;

/**
 * Implementation of the InsightFactory interface for managing user data within a MongoDB database.
 * Provides methods to query, save, update, and delete user information.
 */
public class InsightFactory_Impl implements InsightFactory {

    private MongoDBConnectionHandler dbConnectionHandler = null;

    private MongoCollection<Document> userCollection ;

    private MongoCollection<Document> speakerCollection;

    private MongoCollection<Document> commentCollection;

    private MongoCollection<Document> speechCollection;

    private MongoCollection<Document> abgeordnterCollection;

    private MongoCollection<Document> meetingCollection;



    /**
     * Retrieves the MongoDBConnectionHandler for database interactions.
     *
     * @return The current instance of MongoDBConnectionHandler.
     */
    public MongoDBConnectionHandler getMongoConnection() {
        return this.dbConnectionHandler;
    }


    /**
     * Creates a database connection using the provided MongoDB configuration.
     *
     * @param pConfig The MongoDB configuration to use for the connection.
     */
    public void createDatabaseConnection(MongoDBConfig pConfig) {
        this.dbConnectionHandler = new MongoDBConnectionHandler(pConfig);

    }


    /**
     * Adding Speech
     *
     * @param pSpeech
     */
    @Override
    public void addSpeech(Speech pSpeech) {

    }

    /**
     * Adding Comment
     *
     * @param pComment
     */
    @Override
    public void addComment(Comment pComment) {

    }

    /**
     * Add Speaker
     *
     * @param pSpeaker
     */
    @Override
    public void addSpeaker(Speaker pSpeaker) {

    }

    @Override
    public List<Document> findByIds(List<String> commentIdList) {
        this.commentCollection = dbConnectionHandler.getCollection("comment");
        return commentCollection.find(in("_id", commentIdList)).into(new ArrayList<>());
    }

    @Override
    public AggregateIterable<Document> findByIdsWithSpeaker(List<String> commentIdList) {
        this.commentCollection = dbConnectionHandler.getCollection("comment");
        return commentCollection.aggregate(Arrays.asList(
                // 首先匹配符合条件的speech记录
                new Document("$match", in("_id", commentIdList)),

                // 将speech中的speaker字段关联到speaker集合中
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // 将speaker数组拆分为单独的文档
                new Document("$unwind", "$speakerInfo"),
                // 可以添加其他聚合操作，如筛选、投影等

                // 最后输出结果
                new Document("$project", new Document()
                        .append("text", "$text")
                        .append("speech", "$speech")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                ),
                // 最后输出结果
                new Document("$project", new Document("title", 1)
                        .append("_id", 1)
                        .append("text", 1)
                        .append("speech", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                )
        ));
    }

    @Override
    public List<Document> findByDateDescending(int limit) {
        this.meetingCollection = dbConnectionHandler.getCollection("meeting");
        return meetingCollection.find().sort(descending("date")).limit(limit).into(new ArrayList<>());

    }

    @Override
    public ObjectId save(Document document) {
        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        speakerCollection.insertOne(document);
        return document.getObjectId("_id");
    }

    @Override
    public void updateByFieldMap(ObjectId id, Map<String, Object> fieldMap) {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        Document query = new Document("_id", id);
        Document updateDoc = new Document("$set", fieldMap);
        speakerCollection.updateOne(query, updateDoc);
    }

    @Override
    public void updateByFieldMap(String id, Map<String, Object> fieldMap) {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        Document query = new Document("_id", id);
        Document updateDoc = new Document("$set", fieldMap);
        speakerCollection.updateOne(query, updateDoc);
    }

    @Override
    public boolean updateSpeakerById(ObjectId id, String json) {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        Document query = new Document("_id", id); // 替换成你实际的查询条件
        Document update = new Document("$set", Document.parse(json));
        UpdateResult updateResult = speakerCollection.updateOne(query, update);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public List<Document> findAllSpeaker() {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        return speakerCollection.find().sort(descending("name")).into(new ArrayList<>());
    }

    @Override
    public Document findSpeakerById(String id) {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        return speakerCollection.find(eq("_id", id)).first();
    }

    @Override
    public Document findByIdAggregate(String id) {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");

        this.abgeordnterCollection = dbConnectionHandler.getCollection("Abgeordneter");
        Document speakerDocument = speakerCollection.find(new Document("_id", id)).first();
        Document abgeordnterDocument = abgeordnterCollection.find(new Document("_id", id)).first();

        if (abgeordnterDocument == null) {
            return speakerDocument;
        } else {
            speakerDocument.putAll(abgeordnterDocument);
            return speakerDocument;
        }
    }

    @Override
    public List<Document> searchSpeaker(String name, String firstName, String fraction, String party) {

        this.speakerCollection = dbConnectionHandler.getCollection("speaker");
        Document query = new Document();

        if (name != null && !name.isEmpty()) {
            query.append("name", new Document("$regex", name));
        }

        // 添加 firstName 参数的模糊查询条件
        if (firstName != null && !firstName.isEmpty()) {
            query.append("firstName", new Document("$regex", firstName));
        }

        // 添加 party 参数的模糊查询条件
        if (party != null && !party.isEmpty()) {
            query.append("party", new Document("$regex", party));
        }

        if (fraction != null && !fraction.isEmpty()) {
            query.append("fraction", new Document("$regex", fraction));
        }

        List<Document> speakers = speakerCollection.find(query).sort(descending("name")).into(new ArrayList<>());

        return speakers;
    }

    @Override
    public AggregateIterable<Document> aggregate() {
        this.speechCollection = dbConnectionHandler.getCollection("speech");
        return speechCollection.aggregate(Arrays.asList(
                // 首先匹配符合条件的speech记录
                //new Document("$match", new Document("someCondition", "someValue")),

                // 将speech中的speaker字段关联到speaker集合中
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // 将speaker数组拆分为单独的文档
                new Document("$unwind", "$speakerInfo"),
                        /*
                        new Document("$lookup", new Document()
                                .append("from", "comment")
                                .append("localField", "comments")
                                .append("foreignField", "_id")
                                .append("as", "commentInfo")
                        )
                        */
                // 可以添加其他聚合操作，如筛选、投影等

                // 最后输出结果
                new Document("$project", new Document()
                        .append("title", "$protocol.title")
                        .append("date", "$protocol.date")
                        .append("starttime", "$protocol.starttime")
                        .append("endtime", "$protocol.endtime")
                        .append("place", "$protocol.place")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                        .append("index", "$agenda.index")

                ),
                // 最后输出结果
                new Document("$project", new Document("title", 1)
                        .append("date", 1)
                        .append("starttime", 1)
                        .append("endtime", 1)
                        .append("place", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                        .append("index", 1)
                )
        ));
    }

    @Override
    public AggregateIterable<Document> searchSpeech(Date starttime, Date endtime) {

        Document query = new Document();

        if (starttime != null) {
            query.append("protocol.starttime", new Document("$gte", starttime.getTime()));
        }

        if (endtime != null) {
            query.append("protocol.endtime", new Document("$lte", endtime.getTime()));
        }

        return aggregateQuery(query);
    }

    @Override
    public AggregateIterable<Document> queryDownloadSpeeches(String protocol) {
        Document query = new Document("protocol.title", protocol);
        return downloadAggregateQuery(query);
    }

    @Override
    public AggregateIterable<Document> downloadAggregateQuery(Document query) {
        this.speechCollection = dbConnectionHandler.getCollection("speech");
        return speechCollection.aggregate(Arrays.asList(
                // 首先匹配符合条件的speech记录
                new Document("$match", query),

                // 将speech中的speaker字段关联到speaker集合中
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // 将speaker数组拆分为单独的文档
                new Document("$unwind", "$speakerInfo"),
                        /*
                        new Document("$lookup", new Document()
                                .append("from", "comment")
                                .append("localField", "comments")
                                .append("foreignField", "_id")
                                .append("as", "commentInfo")
                        )
                        */
                // 可以添加其他聚合操作，如筛选、投影等

                // 最后输出结果
                new Document("$project", new Document()
                        .append("title", "$protocol.title")
                        .append("date", "$protocol.date")
                        .append("starttime", "$protocol.starttime")
                        .append("endtime", "$protocol.endtime")
                        .append("place", "$protocol.place")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                        .append("index", "$agenda.index")
                        .append("text", "$text")
                        .append("comments", "$comments")
                ),
                // 最后输出结果
                new Document("$project", new Document("title", 1)
                        .append("date", 1)
                        .append("starttime", 1)
                        .append("endtime", 1)
                        .append("place", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                        .append("index", 1)
                        .append("text", 1)
                        .append("comments", 1)
                )
        ));
    }

    @Override
    public AggregateIterable<Document> aggregateQuery(Document query) {
        this.speechCollection = dbConnectionHandler.getCollection("speech");
        return speechCollection.aggregate(Arrays.asList(
                // 首先匹配符合条件的speech记录
                new Document("$match", query),

                // 将speech中的speaker字段关联到speaker集合中
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // 将speaker数组拆分为单独的文档
                new Document("$unwind", "$speakerInfo"),
                        /*
                        new Document("$lookup", new Document()
                                .append("from", "comment")
                                .append("localField", "comments")
                                .append("foreignField", "_id")
                                .append("as", "commentInfo")
                        )
                        */
                // 可以添加其他聚合操作，如筛选、投影等

                // 最后输出结果
                new Document("$project", new Document()
                        .append("title", "$protocol.title")
                        .append("date", "$protocol.date")
                        .append("starttime", "$protocol.starttime")
                        .append("endtime", "$protocol.endtime")
                        .append("place", "$protocol.place")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                        .append("index", "$agenda.index")
                ),
                // 最后输出结果
                new Document("$project", new Document("title", 1)
                        .append("date", 1)
                        .append("starttime", 1)
                        .append("endtime", 1)
                        .append("place", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                        .append("index", 1)
                )
        ));
    }

    @Override
    public boolean updateSpeechById(String id, String json) {
        this.speechCollection = dbConnectionHandler.getCollection("speech");
        Document query = new Document("_id", id); // 替换成你实际的查询条件
        Document update = new Document("$set", Document.parse(json));
        UpdateResult updateResult = speechCollection.updateOne(query, update);
        return updateResult.getModifiedCount() > 0;
    }

    @Override
    public Document findSpeechById(String id) {
        this.speechCollection = dbConnectionHandler.getCollection("speech");
        // 查询并投影

        Document result = speechCollection.aggregate(
                Arrays.asList(
                        match(eq("_id", id)), // 根据 id 进行匹配
                        new Document("$project", new Document()
                                .append("title", "$protocol.title")
                                .append("speaker", "$speaker")
                                .append("starttime", "$protocol.starttime")
                                .append("endtime", "$protocol.endtime")
                                .append("place", "$protocol.place")
                                .append("text", "$text")
                                .append("comments", "$comments")
                                .append("length", "$length")
                        ),
                        // 最后输出结果
                        new Document("$project", new Document("_id", 1)
                                .append("title", 1)
                                .append("speaker", 1)
                                .append("text", 1)
                                .append("comments", 1)
                                .append("length", 1)
                                .append("starttime", 1)
                                .append("endtime", 1)
                                .append("place", 1)
                        )
                )
        ).first();

        return result;
    }

    @Override
    public AggregateIterable<Document> globalQueryByKeyword(String keyword) {
        String regexKeyword = ".*" + Pattern.quote(keyword) + ".*";
        Document query = new Document("text", new Document("$regex", regexKeyword));
        return aggregateQuery(query);
    }
}



