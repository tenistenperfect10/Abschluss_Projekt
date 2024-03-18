package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.apache.uima.jcas.JCas;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.texttechnology.parliament_browser_6_4.data.*;
import org.w3c.dom.Node;

public class Comment_Impl extends PlenaryObject_Impl implements Comment {

    private Speaker pSpeaker = null;
    private Speech pSpeech = null;
    private String sText = "";

    /**
     * Constructor based on a Node, calling the super-constructor
     * @param pNode
     */
    public Comment_Impl(Node pNode){
         pNode.getTextContent();
    }

    public Comment_Impl(InsightFactory fac) {
        super(fac);
        
    }


    /**
     * Return the speech the text belongs to
     *
     * @return
     */
    @Override
    public Speech getSpeech() {
        return this.pSpeech;
    }

    /**
     * Return the speaker of this text
     *
     * @return
     */
    @Override
    public Speaker getSpeaker() {
        return this.pSpeaker;
    }

    /**
     * Set the speech the text belongs to
     *
     * @param pSpeech
     */
    @Override
    public void setSpeech(Speech pSpeech) {

        this.pSpeech = pSpeech;

    }

    /**
     * Set the speaker of this text
     *
     * @param pSpeaker
     */
    @Override
    public void setSpeaker(Speaker pSpeaker) {

        this.pSpeaker = pSpeaker;

    }

    /**
     * Return the content of this text
     *
     * @return
     */
    @Override
    public String getContent() {
        return this.sText;
    }
    


    /**
     * well, the hash function
     * @return
     */
    @Override
    public int hashCode() {
        return this.getContent().hashCode();
    }



    /**
     * Return the ID of the Object
     *
     * @return
     */
    @Override
    public String getID() {

        return this.getSpeech().getID()+"-"+this.getContent().hashCode();
    }
}
