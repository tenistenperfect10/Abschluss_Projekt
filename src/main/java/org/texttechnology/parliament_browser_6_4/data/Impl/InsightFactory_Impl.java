package org.texttechnology.parliament_browser_6_4.data.Impl;

import cn.hutool.json.JSONUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConfig;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConnectionHandler;

import java.util.ArrayList;
import java.util.List;
/**
 * Implementation of the InsightFactory interface for managing user data within a MongoDB database.
 * Provides methods to query, save, update, and delete user information.
 */
public class InsightFactory_Impl implements InsightFactory {

    private MongoDBConnectionHandler dbConnectionHandler = null;

    private MongoCollection<Document> userCollection ;


    /**
     * Gets the MongoDB connection handler for interacting with the database.
     *
     * @return The MongoDBConnectionHandler instance.
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
}
