package org.texttechnology.parliament_browser_6_4.data;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Provides the interface for managing user insights within the application.
 * This includes querying, finding, saving, matching, updating, and deleting user data.
 */
public interface InsightFactory {

    /**
     * Queries for the existence of a user by their username.
     *
     * @param username The username of the user to query.
     * @return A Document object representing the found user; null if not found.
     */
    Document queryExistUser(String username);

    /**
     * Finds all users who are categorized as regular users.
     *
     * @return A list of Document objects, each representing a regular user.
     */
    List<Document> findAllRegularUsers();

    /**
     * Saves a new user or updates an existing user in the database.
     *
     * @param user The User_Impl object representing the user to save.
     */
    void saveUser(User_Impl user);

    /**
     * Matches a given user against stored user profiles.
     *
     * @param user The User_Impl object to match.
     * @return true if a match is found; false otherwise.
     */
    boolean matchUser(User_Impl user);

    /**
     * Updates the password for a given user.
     *
     * @param user The User_Impl object representing the user whose password is to be updated.
     * @return true if the update is successful; false otherwise.
     */
    boolean updateUserPwd(User_Impl user);

    /**
     * Updates the permissions for a given user.
     *
     * @param user The User_Impl object representing the user whose permissions are to be updated.
     * @return true if the update is successful; false otherwise.
     */
    boolean updateUserPermission(User_Impl user);

    /**
     * Deletes a user from the database by their username.
     *
     * @param username The username of the user to delete.
     * @return true if the user is successfully deleted; false otherwise.
     */
    boolean deleteUserByUsername(String username);

    AggregateIterable<Document> findByIdsWithSpeaker(List<String> commentIdList);

    List<Document> findByDateDescending(int limit);

    ObjectId save(Document document);

    void updateByFieldMap(ObjectId id, Map<String, Object> fieldMap);

    void updateByFieldMap(String id, Map<String, Object> fieldMap);

    boolean updateSpeakerById(ObjectId id, String json);

    List<Document> findAll();

    Document findById(String id);

    Document findByIdAggregate(String id);

    List<Document> search(String name, String firstName, String fraction, String party);

    AggregateIterable<Document> globalQueryByKeyword(String keyword);

    Document findBySpeechId(String id);

    boolean updateSpeechById(String id, String json);

    AggregateIterable<Document> aggregateQuery(Document query);

    AggregateIterable<Document> searchSpeech(Date starttime, Date endtime);

    AggregateIterable<Document> aggregate();




}
