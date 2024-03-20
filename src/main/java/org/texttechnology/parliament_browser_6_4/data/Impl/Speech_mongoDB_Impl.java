package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.Speech_mongoDB;

import java.util.ArrayList;
import java.util.List;

/**
 *implemention of Interface speech_mongoDB
 */
public class Speech_mongoDB_Impl implements Speech_mongoDB {
    private String speaker = null;

    private String text = null;

    private List<String> comments = new ArrayList<>(0);

    private Document doc = null;

    private InsightFactory fac = null;

    /**
     * main method
     * @param speaker
     * @param text
     * @param comments
     */
    public Speech_mongoDB_Impl(String speaker, String text, List<String> comments) {
        this.speaker = speaker;
        this.text = text;
        this.comments = comments;
    }
    public Speech_mongoDB_Impl(InsightFactory fac,Document doc){
        this.fac = fac;
        this.doc = doc;
    }

    public Speech_mongoDB_Impl() {
    }

    /**
     * get the speaker
     * @return
     */

    @Override
    public String getSpeaker() {
        return this.speaker;
    }

    /**
     * set the speaker
     * @param speaker
     */

    @Override
    public void setSpeaker(String speaker) {

        this.speaker = speaker;

    }

    /**
     * get the text
     * @return
     */

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * set the text
     * @param text
     */

    @Override
    public void setText(String text) {

        this.text = text;

    }

    /**
     * get the list of comments
     * @return
     */

    @Override
    public List<String> getComments() {
        return this.comments;
    }

    /**
     * set the list of comments
     * @param comments
     */

    @Override
    public void setComments(List<String> comments) {

        this.comments = comments;

    }
}
