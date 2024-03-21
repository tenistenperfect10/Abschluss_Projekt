package org.texttechnology.parliament_browser_6_4.dao;


import cn.hutool.json.JSONUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code UserDAO} class is responsible for interacting with the MongoDB collection that stores user information.
 * It provides functionality to query, save, update, and delete users in the database. This class is utilized by
 * the application to perform operations related to user management.
 *
 * @author He Liu
 * @author Yu Ming
 */
public class UserDAO {

    private final MongoCollection<Document> userCollection;

    /**
     * Constructs a UserDAO instance with a connection to the MongoDB database.
     *
     * @param database The MongoDB database instance.
     */
    public UserDAO(final MongoDatabase database) {
        userCollection = database.getCollection("user");
    }

    /**
     * Queries the database for a user with the specified username.
     *
     * @param username The username to query.
     * @return A {@link Document} representing the user, or null if the user does not exist.
     */
    public Document queryExistUser(String username) {
        return userCollection.find(new Document("username", username)).first();
    }


    /**
     * Retrieves all regular users (non-admins) from the database.
     *
     * @return A list of {@link Document} objects representing the regular users.
     */

    public List<Document> findAllRegularUsers() {
        return userCollection.find(new Document("userType", 0)).into(new ArrayList<>());
    }

    /**
     * Saves a new user to the database.
     *
     * @param user The {@link User_Impl} object representing the user to save.
     */
    public void saveUser(User_Impl user) {
        Document document = Document.parse(JSONUtil.toJsonStr(user));
        userCollection.insertOne(document);
    }

    /**
     * Checks if the provided user credentials match a user in the database.
     *
     * @param user The {@link User_Impl} object containing the username and password to match.
     * @return True if a matching user is found, false otherwise.
     */
    public boolean matchUser(User_Impl user) {
        Document queryUser = userCollection.find(new Document("username", user.getUsername())).first();
        if (queryUser.getString("password").equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    /**
     * Updates the password for the user with the given username.
     *
     * @param user The {@link User_Impl} object containing the username and the new password.
     * @return True if the update was successful, false otherwise.
     */

    public boolean updateUserPwd(User_Impl user) {
        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("password", user.getPassword()));
        return updateResult.getMatchedCount() > 0;
    }

    /**
     * Updates the user permission for the user with the given username.
     *
     * @param user The {@link User_Impl} object containing the username and the new permission level.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateUserPermission(User_Impl user) {
        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("canEdit", user.getCanEdit()));
        return updateResult.getMatchedCount() > 0;
    }

    /**
     * Deletes the user with the specified username from the database.
     *
     * @param username The username of the user to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteUserByUsername(String username) {
        DeleteResult result = userCollection.deleteOne(new Document("username", username));
        return result.getDeletedCount() > 0;
    }

}
