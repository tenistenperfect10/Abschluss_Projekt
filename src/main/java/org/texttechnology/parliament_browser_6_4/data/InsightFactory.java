package org.texttechnology.parliament_browser_6_4.data;

import com.mongodb.client.AggregateIterable;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;
import org.texttechnology.parliament_browser_6_4.helper.NLPHelper;
import org.w3c.dom.Node;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides the interface for managing user insights within the application.
 * This includes querying, finding, saving, matching, updating, and deleting user data.
 * @author Giuseppe Abrami
 */
public interface InsightFactory {


    /**
     * Return all speakers
     * @return
     */
    Set<Speaker> getSpeakers();

    /**
     * Return all speakers by Fraction
     * @param pFraction
     * @return
     */
    Set<Speaker> getSpeakers(Fraction pFraction);

    /**
     * Return all protocols
     * @return
     */
    Set<PlenaryProtocol> getProtocols();

    Set<Speech> getpSpeeches();

    void addSpeeches(Speech speech);

    /**
     * Get Protocolls by WP
     * @return
     */
    Set<PlenaryProtocol> getProtocols(int iWP);

    /**
     * Add a protocol
     * @param pProtocol
     */
    void addProtocol(PlenaryProtocol pProtocol);

    /**
     * Return all fractions
     * @return
     */
    Set<Fraction> getFractions();

    /**
     * Return all parties
     * @return
     */
    Set<Party> getParties();

    /**
     * Get a specific Party. If the party does not exist, it will be created.
     * @param sName
     * @return
     */
    Party getParty(String sName);

    /**
     * Get a speaker based on its Name. If the speaker does not exist, he / she will be created
     * @param sName
     * @return
     */
    Speaker getSpeaker(String sName);


    void addspeaker(Speaker pSpeaker);

    /**
     * Get a speaker based on a Node. If the speaker does not exist, he / she will be created
     * @param pNode
     * @return
     */
    Speaker getSpeaker(Node pNode);

    /**
     * Get a fraction based on its Name. If the fraction does not exist, it will be created
     * @param sName
     * @return
     */
    Fraction getFraction(String sName);

    /**
     * Get a fraction based on a Node. If the fraction does not exist, it will be created
     * @param pNode
     * @return
     */
    Fraction getFraction(Node pNode);

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



    /**
     * Get all speeches
     * @return
     */
    List<Speech> getSpeeches();




    /**
     * Get all comments
     * @return
     */
    List<Comment> getComments();


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

    /**
     * Get the NLP Helper
     * @return
     */
    NLPHelper getNLPHelper();





}
