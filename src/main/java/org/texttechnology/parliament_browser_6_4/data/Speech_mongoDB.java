package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;

/**
 * Getting the data needed to import latex from the database
 */
public interface Speech_mongoDB {
    /**
     * get the name of speaker
     * @return
     */
    String getSpeaker();

    /**
     * set the name of speaker
     * @param speaker
     */

    void setSpeaker(String speaker);

    /**
     * get the text of speech
     * @return
     */
    String getText();

    /**
     * set the text of speech
     * @param text
     */

    void setText(String text);

    /**
     * get the list of comments
     * @return
     */

    List<String> getComments();

    /**
     * set the list of comments
     * @param comments
     */

    void setComments(List<String> comments);
}
