package org.texttechnology.parliament_browser_6_4.data;

public interface Comment extends PlenaryObject{

    /**
     * Return the speech the text belongs to
     * @return
     */
    Speech getSpeech();

    /**
     * Return the speaker of this text
     * @return
     */
    Speaker getSpeaker();

    /**
     * Set the speech the text belongs to
     * @param pSpeech
     */
    void setSpeech(Speech pSpeech);

    /**
     * Set the speaker of this text
     * @param pSpeaker
     */
    void setSpeaker(Speaker pSpeaker);

    /**
     * Return the content of this text
     * @return
     */
    String getContent();
}
