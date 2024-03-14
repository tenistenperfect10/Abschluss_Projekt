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
    /**
     * Finds comments by their associated speaker IDs.
     *
     * @param commentIdList The list of comment IDs to find the speakers for.
     * @return An iterable of Document objects where each document represents a comment with its associated speaker's information.
     */

    AggregateIterable<Document> findByIdsWithSpeaker(List<String> commentIdList);


    /**
     * Retrieves a list of documents sorted by date in descending order, limited to a specified number.
     *
     * @param limit The maximum number of documents to return.
     * @return A list of Document objects sorted by date in descending order.
     */
    List<Document> findByDateDescending(int limit);

    /**
     * Saves a given document to the database.
     *
     * @param document The document to be saved.
     * @return The ObjectId of the saved document.
     */
    ObjectId save(Document document);
    /**
     * Updates a document identified by an ObjectId with the given field values.
     *
     * @param id The ObjectId of the document to update.
     * @param fieldMap A map containing field names and their new values for the update.
     */
    void updateByFieldMap(ObjectId id, Map<String, Object> fieldMap);

    /**
     * Updates a document identified by a String id with the given field values.
     *
     * @param id The String id of the document to update.
     * @param fieldMap A map containing field names and their new values for the update.
     */
    void updateByFieldMap(String id, Map<String, Object> fieldMap);

    /**
     * Updates the speaker information of a document by its ObjectId.
     *
     * @param id The ObjectId of the document to update.
     * @param json The new speaker information in JSON format.
     * @return true if the update is successful; false otherwise.
     */
    boolean updateSpeakerById(ObjectId id, String json);


    /**
     * Finds all documents in the database.
     *
     * @return A list of all documents.
     */
    List<Document> findAll();

    /**
     * Finds a document by its String id.
     *
     * @param id The String id of the document to find.
     * @return The found document, or null if no document matches the id.
     */
    Document findById(String id);

    /**
     * Performs an aggregate query to find a document by its String id.
     *
     * @param id The String id of the document to find.
     * @return The found document, or null if no document matches the id.
     */
    Document findByIdAggregate(String id);

    /**
     * Searches for documents matching the given parameters.
     *
     * @param name The name to search for.
     * @param firstName The first name to search for.
     * @param fraction The fraction to search for.
     * @param party The party to search for.
     * @return A list of documents matching the search criteria.
     */
    List<Document> search(String name, String firstName, String fraction, String party);


    /**
     * Performs a global query by a keyword.
     *
     * @param keyword The keyword to search for.
     * @return An iterable of documents matching the keyword.
     */
    AggregateIterable<Document> globalQueryByKeyword(String keyword);

    /**
     * Finds a document by a speech ID.
     *
     * @param id The speech ID of the document to find.
     * @return The found document, or null if no document matches the speech ID.
     */
    Document findBySpeechId(String id);
    /**
     * Updates a speech document by its ID.
     *
     * @param id The ID of the speech document to update.
     * @param json The new speech information in JSON format.
     * @return true if the update is successful; false otherwise.
     */
    boolean updateSpeechById(String id, String json);
    /**
     * Performs an aggregate query.
     *
     * @param query The aggregate query to perform.
     * @return An iterable of documents that match the aggregate query.
     */
    AggregateIterable<Document> aggregateQuery(Document query);

    /**
     * Searches for speeches within a specified date range.
     *
     * @param starttime The start time of the date range.
     * @param endtime The end time of the date range.
     * @return An iterable of documents representing speeches within the specified date range.
     */
    AggregateIterable<Document> searchSpeech(Date starttime, Date endtime);
    /**
     * Performs an aggregate operation without any specific criteria.
     *
     * @return An iterable of all documents resulting from the aggregation.
     */
    AggregateIterable<Document> aggregate();




}
