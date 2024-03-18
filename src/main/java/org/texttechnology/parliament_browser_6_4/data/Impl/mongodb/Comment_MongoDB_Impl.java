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
import org.texttechnology.parliament_browser_6_4.data.Comment;
import org.texttechnology.parliament_browser_6_4.data.Impl.file.Comment_File_Impl;
import org.texttechnology.parliament_browser_6_4.data.Impl.file.Text_File_Impl;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;

import org.w3c.dom.Node;

import java.util.List;
import java.util.stream.Collectors;

public class Comment_MongoDB_Impl extends Text_File_Impl implements Comment, CasProcesser {

    private JCas jCas = null;
    private Document doc = null;


    public Comment_MongoDB_Impl(InsightFactory fac, Document doc){
        super(fac);
        this.doc = doc;
    }

    @Override
    public String getID() {
        return doc.getString("_id");
    }

    @Override
    public String getContent() {
        return doc.getString("text");
    }



    @Override
    public JCas toCas()  {
        if(this.jCas == null){
            try {
                jCas = JCasFactory.createText(doc.getString("text"),"de");
            } catch (UIMAException e) {
                throw new RuntimeException(e);
            }

        }
        return this.jCas;
    }

    @Override
    public void setCas(JCas jCas) {
        this.jCas = jCas;
    }

    @Override
    public List<Token> getTokens() {
        return JCasUtil.select(this.toCas(), Token.class).stream().collect(Collectors.toList());
    }

    @Override
    public List<Sentence> getSentences() {
            return JCasUtil.select(this.toCas(), Sentence.class).stream().collect(Collectors.toList());
    }

    @Override
    public List<Dependency> getDependency() {

            return JCasUtil.select(this.toCas(), Dependency.class).stream().collect(Collectors.toList());

    }

    @Override
    public List<POS> getPOS() {

            return JCasUtil.select(this.toCas(), POS.class).stream().collect(Collectors.toList());

    }

    @Override
    public List<NamedEntity> getNamedEntities() {

            return JCasUtil.select(this.toCas(), NamedEntity.class).stream().collect(Collectors.toList());


    }

    @Override
    public Sentiment getDocSentiment() {
        return null;
    }

    @Override
    public List<Sentiment> getSentiments() {

        return JCasUtil.select(this.toCas(), Sentiment.class).stream().collect(Collectors.toList());
    }

}
