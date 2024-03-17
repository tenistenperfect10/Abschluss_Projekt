package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.Speech_mongoDB;

import java.util.List;

public class Speech_mongoDB_Impl implements Speech_mongoDB {
    private String speaker;

    private String text;

    private List<String> comments;

    public Speech_mongoDB_Impl(String speaker, String text, List<String> comments) {
        this.speaker = speaker;
        this.text = text;
        this.comments = comments;
    }

    public Speech_mongoDB_Impl() {
    }

    @Override
    public String getSpeaker() {
        return this.speaker;
    }

    @Override
    public void setSpeaker(String speaker) {

        this.speaker = speaker;

    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {

        this.text = text;

    }

    @Override
    public List<String> getComments() {
        return this.comments;
    }

    @Override
    public void setComments(List<String> comments) {

        this.comments = comments;

    }
}
