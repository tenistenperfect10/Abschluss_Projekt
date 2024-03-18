package org.texttechnology.parliament_browser_6_4.helper;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.TypeSystemUtil;
import org.bson.BsonDocument;
import org.bson.Document;
import org.hucompute.textimager.uima.type.Sentiment;
import org.junit.jupiter.api.Test;
import org.texttechnology.parliament_browser_6_4.data.CasProcesser;
import org.texttechnology.parliament_browser_6_4.data.Comment;
import org.texttechnology.parliament_browser_6_4.data.Impl.InsightFactory_Impl;
import org.texttechnology.parliament_browser_6_4.data.Speech;
import org.texttechnologylab.DockerUnifiedUIMAInterface.DUUIComposer;
import org.texttechnologylab.DockerUnifiedUIMAInterface.driver.DUUIRemoteDriver;
import org.texttechnologylab.DockerUnifiedUIMAInterface.lua.DUUILuaContext;
import org.texttechnologylab.DockerUnifiedUIMAInterface.pipeline_storage.sqlite.DUUISqliteStorageBackend;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * Processing the NLP-Analyse
 */
public class NLPHelper {

    private DUUIComposer duuiComposer = null;

    private static final Logger LOGGER = Logger.getLogger(NLPHelper.class.getName());

    private MongoDBConnectionHandler db = new MongoDBConnectionHandler(new MongoDBConfig("target\\classes\\Project_06_04.txt"));
    private InsightFactory_Impl factory = new InsightFactory_Impl(db);

    /**
     * Constructor
     * @throws Exception
     */
    public NLPHelper() throws Exception {

        // Führe den Task mit zwei Prozessen (Instanzen) aus.
        int iWorkers = 1;

        DUUISqliteStorageBackend sqlite = new DUUISqliteStorageBackend("statistic.db")
                .withConnectionPoolSize(iWorkers);

        // Lua-Kontext für die Nutzung von Lua
        DUUILuaContext ctx = new DUUILuaContext().withJsonLibrary();

        // Instanziierung des Composers, mit einigen Parametern
        DUUIComposer composer = new DUUIComposer()
                .withSkipVerification(true)     // wir überspringen die Verifikation aller Componenten =)
                .withLuaContext(ctx)            // wir setzen den definierten Kontext
                .withStorageBackend(sqlite)     // SQLite-Backend (optional)
                .withWorkers(iWorkers);         // wir geben dem Composer eine Anzahl an Threads mit.


        DUUIRemoteDriver remoteDriver = new DUUIRemoteDriver();

        composer.addDriver(remoteDriver);
        composer.add(new DUUIRemoteDriver.Component("http://127.0.0.1:1000").withScale(iWorkers).build());
        composer.add(new DUUIRemoteDriver.Component("http://127.0.0.1:1001").withScale(iWorkers).build());
        composer.add(new DUUIRemoteDriver.Component("http://127.0.0.1:1002").withScale(iWorkers).build());

        duuiComposer = composer;
    }



    public DUUIComposer getDuuiComposer(){
        return this.duuiComposer;
    }

    /**
     * Method to process the Text of a speech
     * @param speech
     * @return
     * @throws Exception
     */
    public JCas analyse(Speech speech) throws Exception {

        CasProcesser sCas = (CasProcesser) speech;
        JCas pCas = sCas.toCas();
        this.duuiComposer.run(pCas,"test");

        TypeSystemDescription desc = TypeSystemUtil.typeSystem2TypeSystemDescription(pCas.getTypeSystem());

        CAS cas = CasCreationUtils.createCas(desc, null, null, null);
        OutputStream out = new ByteArrayOutputStream();
        XmiCasSerializer.serialize(pCas.getCas(), out);
        byte[] byteArray = ((ByteArrayOutputStream) out).toByteArray();
        InputStream inputStream = new ByteArrayInputStream(byteArray);

        XmiCasDeserializer.deserialize(inputStream, cas);

        JCas mycas = cas.getJCas();

        sCas.setCas(mycas);
        return mycas;
    }

    /**
     * Method zu process the Text of a comment
     * @param comment
     * @return
     * @throws Exception
     */
    public JCas analyse(Comment comment) throws Exception {
        CasProcesser sCas = (CasProcesser) comment;
        JCas pCas = sCas.toCas();
        this.duuiComposer.run(pCas,"test");

        TypeSystemDescription desc = TypeSystemUtil.typeSystem2TypeSystemDescription(pCas.getTypeSystem());

        CAS cas = CasCreationUtils.createCas(desc, null, null, null);
        OutputStream out = new ByteArrayOutputStream();
        XmiCasSerializer.serialize(pCas.getCas(), out);
        byte[] byteArray = ((ByteArrayOutputStream) out).toByteArray();
        InputStream inputStream = new ByteArrayInputStream(byteArray);

        XmiCasDeserializer.deserialize(inputStream, cas);

        JCas mycas = cas.getJCas();

        sCas.setCas(mycas);
        return mycas;
    }

    /**
     * Method to Update NLP-Informations for a speech document
     * If the document has been already processed, read and skip it. If not, process it
     * @throws Exception
     */
    @Test
    public void processSpeechNLP() throws Exception {

        MongoCollection collection = this.factory.getMongoConnection().getCollection("speech");

        List<Speech> sList = this.factory.getSpeeches();

        int countAll = (int) collection.count();
        int count = 0;
        int skip = 500;

        JCas finalMycas = null;
        Document nlpDocument = null;

        while (skip*count < countAll) {
            try(MongoCursor<Document> documents = collection.find(BsonDocument.parse("{}")).skip(skip*count).limit(skip).iterator()) {
                while (documents.hasNext()) {

                    for (Speech speech : sList) {
                        finalMycas = analyse(speech);
                        LOGGER.log(Level.INFO, "Final JCas content: {0}", finalMycas.toString());
                        System.out.println("Final JCas content: " + finalMycas);

                        nlpDocument = NLPDocument(finalMycas);
                        String sID = speech.getID();
                        System.out.println("NLP Document: " + nlpDocument.toJson());

                        collection.updateOne(eq("_id", sID), new Document("$set", nlpDocument));

                    }
                }
            } finally {

                finalMycas = null;
                nlpDocument = null;
            }
            count ++;
        }
    }

    /**
     * Method to Update NLP-Informations for a comment document
     * If the document has been already processed, read and skip it. If not, process it
     * @throws Exception
     */
    @Test
    public void processCommentNLP() throws Exception {

        MongoCollection collection = factory.getMongoConnection().getCollection("comment");
        List<Comment> cList = factory.getComments();
        int countAll = (int) collection.count();
        int count = 0;
        int skip = 500;

        JCas finalMycas = null;
        Document nlpDocument = null;

        while (skip*count < countAll) {
            try(MongoCursor<Document> documents = collection.find(BsonDocument.parse("{}")).skip(skip*count).limit(skip).iterator()) {
                while (documents.hasNext()) {

                    for (Comment comment : cList) {
                        finalMycas = analyse(comment);
                        LOGGER.log(Level.INFO, "Final JCas content: {0}", finalMycas.toString());
                        System.out.println("Final JCas content: " + finalMycas);

                        nlpDocument = NLPDocument(finalMycas);
                        String sID = comment.getID();
                        System.out.println("NLP Document: " + nlpDocument.toJson());

                        collection.updateOne(eq("_id", sID), new Document("$set", nlpDocument));

                    }
                }
            } finally {

                finalMycas = null;
                nlpDocument = null;
            }
            count ++;
        }
    }




    /**
     * Methode to analyse the CAS objects and convert to a Mongodocument
     * @param mycas
     * @return
     */
    public Document NLPDocument(JCas mycas){

        Document update = new Document();
        List<String> sentenceList = new ArrayList<>(0);
        List<String> tokenList = new ArrayList<>(0);
        Set<String> POSSet = new HashSet<>(0);
        List<String> perList = new ArrayList<>(0);
        List<String> locList = new ArrayList<>(0);
        List<String> orgList = new ArrayList<>(0);
        List<Double> sentimentList = new ArrayList<>();
        JCasUtil.select(mycas, Sentence.class).stream().forEach(s -> {

            sentenceList.add(s.getCoveredText());


            for (Token t : JCasUtil.selectCovered(mycas, Token.class, s)) {
                tokenList.add(t.getCoveredText());
            }


            for (POS p : JCasUtil.selectCovered(mycas, POS.class, s)) {
                POSSet.add(p.getPosValue());
            }

            for (NamedEntity entity : JCasUtil.selectCovered(mycas, NamedEntity.class, s)) {
                if (entity.getValue().equals("PER")) {
                    perList.add(entity.getCoveredText());

                } else if (entity.getValue().equals("LOC")) {
                    locList.add(entity.getCoveredText());

                } else if (entity.getValue().equals("ORB")) {
                    orgList.add(entity.getCoveredText());

                }
            }

            for (Sentiment sentiment : JCasUtil.selectCovered(mycas, Sentiment.class, s)) {
                sentimentList.add(sentiment.getSentiment());

            }

        });
        update.append("sentences",sentenceList);
        update.append("token", tokenList);
        update.append("pos",POSSet);
        update.append("persons",  perList);

        update.append("locations", locList);

        update.append("organizations", orgList);
        update.append("sentiment",sentimentList);
        update.append("sentiment for the speech",sentimentList.stream().mapToDouble(Double::doubleValue).sum());


        return update;
    }
}
