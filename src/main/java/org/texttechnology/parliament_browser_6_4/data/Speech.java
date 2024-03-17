package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;

public interface Speech extends PlenaryObject{

    AgendaItem getAgendaItem();

    /**
     * Return all comments in this speech
     * @return
     */
    List<Comment> getComments();

    /**
     * Return the full text of the speech
     * @return
     */
    String getText();

    String getPlainText();

    /**
     * Return the plenary protocol this speechs belongs to
     * @return
     */
    PlenaryProtocol getProtocol();

    /**
     * Return the speaker of tis speech
     * @return
     */
    Speaker getSpeaker();

    /**
     * Set the speaker of this speech
     * @param pSpeaker
     */
    void setSpeaker(Speaker pSpeaker);

    /**
     * Return the length of this speech
     * @return
     */
    int getLength();

    /**
     * Return all insertions which are no comments
     * @return
     */
    List<Speech> getInsertions();


}
