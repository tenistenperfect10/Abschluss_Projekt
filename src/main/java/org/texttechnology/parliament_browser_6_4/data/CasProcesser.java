package org.texttechnology.parliament_browser_6_4.data;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.UIMAException;
import org.apache.uima.jcas.JCas;
import org.hucompute.textimager.uima.type.Sentiment;

import java.util.List;

/**
 *´Interface for NLP
 */
public interface CasProcesser {

    /**
     * Transform to JCas
     * @return
     */
    JCas toCas() throws UIMAException;

    /**
     * Set the Cas
     * @param cas
     */
    void setCas(JCas cas);

    /**
     * Get Tokens
     * @return
     */
    List<Token> getTokens() throws Exception;

    /**
     * Get Sentences
     * @return
     */
    List<Sentence> getSentences() throws Exception;

    /**
     * Get Dependency
     * @return
     */
    List<Dependency> getDependency();

    /**
     * Get POS
     * @return
     */
    List<POS> getPOS();

    /**
     * Get Named Entities
     * @return
     */
    List<NamedEntity> getNamedEntities();

    /**
     * Get the Sentiment of a speech
     * @return
     */
    Sentiment getDocSentiment();

    /**
     * Get Sentiments
     * @return
     */
    List<Sentiment> getSentiments();




}

