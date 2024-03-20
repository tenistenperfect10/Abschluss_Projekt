package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;
import java.util.Map;

/**
 * Setting the data to be imported into the latex file
 */
public interface LatexSpeech {
    /**
     * get the title of latex
     * @return
     */
    String getTitle();

    /**
     * set the title of latex
     * @param title
     */

    void setTitle(String title);

    /**
     * get the map of speech in latex
     * @return
     */

    Map<String, List<Speech_mongoDB>> getSpeechMap();

    /**
     * set the map of speech in latex
     * @param speechMap
     */

    void setSpeechMap(Map<String, List<Speech_mongoDB>> speechMap);
}
