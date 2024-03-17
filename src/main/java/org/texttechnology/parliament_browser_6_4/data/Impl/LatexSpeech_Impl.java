package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.LatexSpeech;
import org.texttechnology.parliament_browser_6_4.data.Speech;
import org.texttechnology.parliament_browser_6_4.data.Speech_mongoDB;

import java.util.List;
import java.util.Map;

public class LatexSpeech_Impl implements LatexSpeech {

    private String title;

    private Map<String, List<Speech_mongoDB>> speechMap;


    public LatexSpeech_Impl(){

    }
    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {

        this.title = title;

    }

    @Override
    public Map<String, List<Speech_mongoDB>> getSpeechMap() {
        return this.speechMap;
    }

    @Override
    public void setSpeechMap(Map<String, List<Speech_mongoDB>> speechMap) {
        this.speechMap = speechMap;

    }

    @Override
    public String toString() {
        return "LatexSpeech{" +
                "title='" + title + '\'' +
                ", speechMap=" + speechMap +
                '}';
    }
}
