package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.LatexSpeech;
import org.texttechnology.parliament_browser_6_4.data.Speech_mongoDB;

import java.util.List;
import java.util.Map;

/**
 * implemention of LatexSpeech
 * @author He Liu
 * @author Yu Ming
 * @author Yingzhu Chen
 */
public class LatexSpeech_Impl implements LatexSpeech {

    private String title;

    private Map<String, List<Speech_mongoDB>> speechMap;


    public LatexSpeech_Impl(){

    }

    /**
     * get the title
     * @return
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * set the title
     * @param title
     */
    @Override
    public void setTitle(String title) {

        this.title = title;

    }

    /**
     * get the map of speech
     * @return
     */
    @Override
    public Map<String, List<Speech_mongoDB>> getSpeechMap() {
        return this.speechMap;
    }

    /**
     * set the map of speech
     * @param speechMap
     */

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
