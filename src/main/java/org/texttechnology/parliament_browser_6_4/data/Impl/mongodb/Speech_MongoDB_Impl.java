package org.texttechnology.parliament_browser_6_4.data.Impl.mongodb;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.dependency.Dependency;
import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.hucompute.textimager.uima.type.Sentiment;
import org.texttechnology.parliament_browser_6_4.data.CasProcesser;
import org.texttechnology.parliament_browser_6_4.data.Impl.file.Speech_File_Impl;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.Speech;

import java.util.List;
import java.util.stream.Collectors;

public class Speech_MongoDB_Impl extends Speech_File_Impl implements Speech, CasProcesser {

    private Document doc = null;

    private JCas sCas = null;

    public Speech_MongoDB_Impl(InsightFactory pFactory, Document doc) {
        super(pFactory);
        this.doc = doc;
    }

    @Override
    public String getID() {
        return doc.getString("_id");
    }

    @Override
    public JCas toCas() throws UIMAException {
        if(this.sCas == null){
            sCas = JCasFactory.createText(doc.getString("text"),"de");

        }
        return this.sCas;
    }

    @Override
    public void setCas(JCas sCas) {
        this.sCas = sCas;
    }

    @Override
    public List<Token> getTokens()  {
        try {
            return JCasUtil.select(this.toCas(), Token.class).stream().collect(Collectors.toList());
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Sentence> getSentences() {
        try {
            return JCasUtil.select(this.toCas(), Sentence.class).stream().collect(Collectors.toList());
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Dependency> getDependency() {
        try {
            return JCasUtil.select(this.toCas(),Dependency.class).stream().collect(Collectors.toList());
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<POS> getPOS() {
        try {
            return JCasUtil.select(this.toCas(), POS.class).stream().collect(Collectors.toList());
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<NamedEntity> getNamedEntities() {
        try {
            return JCasUtil.select(this.toCas(), NamedEntity.class).stream().collect(Collectors.toList());
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sentiment getDocSentiment() {
        return null;
    }

    @Override
    public List<Sentiment> getSentiments() {
        try {
            return JCasUtil.select(this.toCas(), Sentiment.class).stream().collect(Collectors.toList());
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
