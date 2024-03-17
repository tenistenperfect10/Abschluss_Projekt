package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;
import java.util.Map;

public interface LatexSpeech {

    String getTitle();

    void setTitle(String title);

    Map<String, List<Speech_mongoDB>> getSpeechMap();

    void setSpeechMap(Map<String, List<Speech_mongoDB>> speechMap);
}
