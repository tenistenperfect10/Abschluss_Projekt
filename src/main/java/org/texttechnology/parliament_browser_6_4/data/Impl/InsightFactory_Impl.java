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
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
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
     * Queries for an existing user by their username.
     *
     * @param username The username of the user to query.
     * @return A Document representing the user if found, null otherwise.
     */
    @Override
    public Document queryExistUser(String username) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        return userCollection.find(new Document("username", username)).first();
    }

    /**
     * Finds all users classified as regular users (userType = 0).
     *
     * @return A List of Document objects, each representing a regular user.
     */
    @Override
    public List<Document> findAllRegularUsers() {
        this.userCollection = dbConnectionHandler.getCollection("user");

        return userCollection.find(new Document("userType", 0)).into(new ArrayList<>());
    }

    /**
     * Saves a user to the database. If the user already exists, updates their information.
     *
     * @param user The user to save or update.
     */
    @Override
    public void saveUser(User_Impl user) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        Document document = Document.parse(JSONUtil.toJsonStr(user));
        userCollection.insertOne(document);
    }

    /**
     * Checks if the provided user's credentials match those stored in the database.
     *
     * @param user The user whose credentials are to be matched.
     * @return true if the credentials match, false otherwise.
     */
    @Override
    public boolean matchUser(User_Impl user) {

        this.userCollection = dbConnectionHandler.getCollection("user");
        Document queryUser = userCollection.find(new Document("username", user.getUsername())).first();
        if (queryUser.getString("password").equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * Updates the password for a given user.
     *
     * @param user The user whose password is to be updated, containing the new password.
     * @return true if the update was successful, false otherwise.
     */
    @Override
    public boolean updateUserPwd(User_Impl user) {
        this.userCollection = dbConnectionHandler.getCollection("user");

        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("password", user.getPassword()));
        return updateResult.getMatchedCount() > 0;
    }


    /**
     * Updates the permission level of a given user.
     *
     * @param user The user whose permissions are to be updated, containing the new permission level.
     * @return true if the update was successful, false otherwise.
     */
    @Override
    public boolean updateUserPermission(User_Impl user) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("canEdit", user.getCanEdit()));
        return updateResult.getMatchedCount() > 0;
    }

    /**
     * Deletes a user from the database by their username.
     *
     * @param username The username of the user to be deleted.
     * @return true if the deletion was successful, false otherwise.
     */
    @Override
    public boolean deleteUserByUsername(String username) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        DeleteResult result = userCollection.deleteOne(new Document("username", username));
        return result.getDeletedCount() > 0;
    }

    /**
     * Finds documents associated with given comment IDs and includes speaker information from a separate collection.
     *
     * @param commentIdList A list of comment IDs to find documents for.
     * @return An AggregateIterable of Document objects that include both the comment and associated speaker information.
     */
    @Override
    public AggregateIterable<Document> findByIdsWithSpeaker(List<String> commentIdList) {
        this.commentCollection = dbConnectionHandler.getCollection("comment");
        return commentCollection.aggregate(Arrays.asList(
                // First match the eligible speech records
                new Document("$match", in("_id", commentIdList)),

                // Associate the speaker field in speech to the speaker collection
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // Splitting the speaker array into separate documents
                new Document("$unwind", "$speakerInfo"),
                // Other aggregation operations can be added, such as filtering, projection, etc.

                // Final Output
                new Document("$project", new Document()
                        .append("text", "$text")
                        .append("speech", "$speech")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                ),
                // Final Output
                new Document("$project", new Document("title", 1)
                        .append("_id", 1)
                        .append("text", 1)
                        .append("speech", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                )
        ));
    }
    /**
     * Retrieves documents from the 'meeting' collection, sorted by date in descending order, limited by a specified count.
     *
     * @param limit The maximum number of documents to return.
     * @return A list of Document objects, each representing a meeting, sorted by date.
     */
    @Override
    public List<Document> findByDateDescending(int limit) {
        this.meetingCollection = dbConnectionHandler.getCollection("meeting");

        return meetingCollection.find().sort(descending("date")).limit(limit).into(new ArrayList<>());
    }

    /**
     * Saves a document to the 'speaker' collection and returns its generated ObjectId.
     *
     * @param document The Document to be saved.
     * @return The ObjectId of the saved document.
     */
    @Override
    public ObjectId save(Document document) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");

        speakerCollection.insertOne(document);
        return document.getObjectId("_id");
    }
    /**
     * Updates a document in the 'speaker' collection identified by an ObjectId with new field values.
     *
     * @param id The ObjectId of the document to update.
     * @param fieldMap A map containing field names and their new values.
     */
    @Override
    public void updateByFieldMap(ObjectId id, Map<String, Object> fieldMap) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");
        Document query = new Document("_id", id);
        Document updateDoc = new Document("$set", fieldMap);
        speakerCollection.updateOne(query, updateDoc);

    }

    /**
     * Updates a document in the 'speaker' collection identified by a String id with new field values.
     *
     * @param id The String id of the document to update.
     * @param fieldMap A map containing field names and their new values.
     */
    @Override
    public void updateByFieldMap(String id, Map<String, Object> fieldMap) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");
        Document query = new Document("_id", id);
        Document updateDoc = new Document("$set", fieldMap);
        speakerCollection.updateOne(query, updateDoc);

    }


    /**
     * Updates the information of a speaker in the 'speaker' collection by their ObjectId, using JSON format for the new data.
     *
     * @param id The ObjectId of the speaker document to update.
     * @param json The new data for the speaker in JSON format.
     * @return True if the update modifies at least one document; false otherwise.
     */
    @Override
    public boolean updateSpeakerById(ObjectId id, String json) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");

        Document query = new Document("_id", id); // Replace it with your actual query
        Document update = new Document("$set", Document.parse(json));
        UpdateResult updateResult = speakerCollection.updateOne(query, update);
        return updateResult.getModifiedCount() > 0;
    }

    /**
     * Retrieves all documents from the 'speaker' collection, sorted by name in descending order.
     *
     * @return A list of all speaker documents, sorted by name.
     */
    @Override
    public List<Document> findAll() {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");

        return speakerCollection.find().sort(descending("name")).into(new ArrayList<>());
    }

    /**
     * Finds a document in the 'speaker' collection by its String id.
     *
     * @param id The String id of the document to find.
     * @return The found document, or null if no document matches the id.
     */
    @Override
    public Document findById(String id) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");

        return speakerCollection.find(eq("_id", id)).first();
    }

    /**
     * Performs an aggregate find operation to locate a document by its String id, potentially merging information from multiple collections.
     *
     * @param id The String id of the document to locate.
     * @return The found document, enhanced with additional information if available; otherwise, returns just the found document.
     */
    @Override
    public Document findByIdAggregate(String id) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");

        abgeordnterCollection = this.dbConnectionHandler.getCollection("Abgeordneter");


        Document speakerDocument = speakerCollection.find(new Document("_id", id)).first();
        Document abgeordnterDocument = abgeordnterCollection.find(new Document("_id", id)).first();

        if (abgeordnterDocument == null) {
            return speakerDocument;
        } else {
            speakerDocument.putAll(abgeordnterDocument);
            return speakerDocument;
        }
    }
    /**
     * Searches the 'speaker' collection for documents that match the given criteria, supporting partial matches.
     *
     * @param name The name to search for.
     * @param firstName The first name to search for.
     * @param fraction The fraction to search for.
     * @param party The party to search for.
     * @return A list of Document objects that match the search criteria.
     */
    @Override
    public List<Document> search(String name, String firstName, String fraction, String party) {
        speakerCollection = this.dbConnectionHandler.getCollection("speaker");

        Document query = new Document();

        if (name != null && !name.isEmpty()) {
            query.append("name", new Document("$regex", name));
        }

        // Fuzzy query with firstName parameter
        if (firstName != null && !firstName.isEmpty()) {
            query.append("firstName", new Document("$regex", firstName));
        }

        // Adding fuzzy query conditions for the party parameter
        if (party != null && !party.isEmpty()) {
            query.append("party", new Document("$regex", party));
        }

        if (fraction != null && !fraction.isEmpty()) {
            query.append("fraction", new Document("$regex", fraction));
        }

        List<Document> speakers = speakerCollection.find(query).sort(descending("name")).into(new ArrayList<>());

        return speakers;

    }
    /**
     * Performs a global search by keyword across all documents, using a regex pattern for the search.
     *
     * @param keyword The keyword to search for.
     * @return An AggregateIterable of Document objects that match the keyword.
     */
    @Override
    public AggregateIterable<Document> globalQueryByKeyword(String keyword) {
        String regexKeyword = ".*" + Pattern.quote(keyword) + ".*";
        Document query = new Document("text", new Document("$regex", regexKeyword));
        return aggregateQuery(query);
    }

    /**
     * Finds a document in the 'speech' collection by its speech ID, enriching it with additional information from related collections.
     *
     * @param id The speech ID of the document to find.
     * @return The enriched document, or null if no matching document is found.
     */
    @Override
    public Document findBySpeechId(String id) {
        speechCollection = this.dbConnectionHandler.getCollection("speech");

        Document result = speechCollection.aggregate(
                Arrays.asList(
                        match(eq("_id", id)), // Match by id
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
                        // Final Output
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

    /**
     * Updates a speech document in the 'speech' collection by its ID, using JSON format for the new data.
     *
     * @param id The ID of the speech document to update.
     * @param json The new data for the speech document in JSON format.
     * @return True if the update modifies at least one document; false otherwise.
     */
    @Override
    public boolean updateSpeechById(String id, String json) {
        speechCollection = this.dbConnectionHandler.getCollection("speech");

        Document query = new Document("_id", id); // Replace it with your actual query
        Document update = new Document("$set", Document.parse(json));
        UpdateResult updateResult = speechCollection.updateOne(query, update);
        return updateResult.getModifiedCount() > 0;
    }

    /**
     * Performs an aggregate query on the 'speech' collection, optionally including lookup operations to merge information from related collections.
     *
     * @param query The Document representing the aggregate query conditions.
     * @return An AggregateIterable of Document objects resulting from the query.
     */
    @Override
    public AggregateIterable<Document> aggregateQuery(Document query) {

        return speechCollection.aggregate(Arrays.asList(
                // First match the eligible speech records
                new Document("$match", query),

                // Associate the speaker field in speech to the speaker collection
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // Splitting the speaker array into separate documents
                new Document("$unwind", "$speakerInfo"),
                        /*
                        new Document("$lookup", new Document()
                                .append("from", "comment")
                                .append("localField", "comments")
                                .append("foreignField", "_id")
                                .append("as", "commentInfo")
                        )
                        */
                // Other aggregation operations can be added, such as filtering, projection, etc.

                // Final Output
                new Document("$project", new Document()
                        .append("title", "$protocol.title")
                        .append("date", "$protocol.date")
                        .append("starttime", "$protocol.starttime")
                        .append("endtime", "$protocol.endtime")
                        .append("place", "$protocol.place")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                ),
                // Final Output
                new Document("$project", new Document("title", 1)
                        .append("date", 1)
                        .append("starttime", 1)
                        .append("endtime", 1)
                        .append("place", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                )
        ));
    }
    /**
     * Searches for speeches within a specified date range, aggregating results and enriching them with data from related collections.
     *
     * @param starttime The start time of the date range.
     * @param endtime The end time of the date range.
     * @return An AggregateIterable of Document objects representing speeches within the date range.
     */
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
    /**
     * Performs a general aggregation operation on the 'speech' collection, potentially including data from related collections.
     *
     * @return An AggregateIterable of Document objects resulting from the aggregation operation.
     */
    @Override
    public AggregateIterable<Document> aggregate() {
        speechCollection = this.dbConnectionHandler.getCollection("speech");
        return speechCollection.aggregate(Arrays.asList(
                // First match the eligible speech records
                //new Document("$match", new Document("someCondition", "someValue")),

                // Associate the speaker field in speech to the speaker collection
                new Document("$lookup", new Document()
                        .append("from", "speaker")
                        .append("localField", "speaker")
                        .append("foreignField", "_id")
                        .append("as", "speakerInfo")
                ),

                // Splitting the speaker array into separate documents
                new Document("$unwind", "$speakerInfo"),
                        /*
                        new Document("$lookup", new Document()
                                .append("from", "comment")
                                .append("localField", "comments")
                                .append("foreignField", "_id")
                                .append("as", "commentInfo")
                        )
                        */
                // Other aggregation operations can be added, such as filtering, projection, etc.

                // Final Output
                new Document("$project", new Document()
                        .append("title", "$protocol.title")
                        .append("date", "$protocol.date")
                        .append("starttime", "$protocol.starttime")
                        .append("endtime", "$protocol.endtime")
                        .append("place", "$protocol.place")
                        .append("speakerName", "$speakerInfo.name")
                        .append("speakerId", "$speakerInfo._id")
                ),
                // Final Output
                new Document("$project", new Document("title", 1)
                        .append("date", 1)
                        .append("starttime", 1)
                        .append("endtime", 1)
                        .append("place", 1)
                        .append("speakerName", 1)
                        .append("speakerId", 1)
                )
        ));
    }
}

