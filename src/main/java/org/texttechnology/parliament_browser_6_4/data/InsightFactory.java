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
     * Adding Speech
     * @param pSpeech
     */
    void addSpeech(Speech pSpeech);

    /**
     * Adding Comment
     * @param pComment
     */
    void addComment(Comment pComment);

    /**
     * Add Speaker
     * @param pSpeaker
     */
    void addSpeaker(Speaker pSpeaker);


    List<Document> findByIds(List<String> commentIdList);

    AggregateIterable<Document> findByIdsWithSpeaker(List<String> commentIdList);

    List<Document> findByDateDescending(int limit);

    ObjectId save(Document document);

    void updateByFieldMap(ObjectId id, Map<String, Object> fieldMap);

    void updateByFieldMap(String id, Map<String, Object> fieldMap);

    boolean updateSpeakerById(ObjectId id, String json);

    List<Document> findAllSpeaker();

    Document findSpeakerById(String id);

    Document findByIdAggregate(String id);

    List<Document> searchSpeaker(String name, String firstName, String fraction, String party);

    AggregateIterable<Document> aggregate();

    AggregateIterable<Document> searchSpeech(Date starttime, Date endtime);

    AggregateIterable<Document> queryDownloadSpeeches(String protocol);

    AggregateIterable<Document> downloadAggregateQuery(Document query);

    AggregateIterable<Document> aggregateQuery(Document query);

    boolean updateSpeechById(String id, String json);

    Document findSpeechById(String id);

    AggregateIterable<Document> globalQueryByKeyword(String keyword);





}
