package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;
import java.util.Set;

public interface AgendaItem extends PlenaryObject{

    /**
     * Get all Speeches
     * @return
     */
    List<Speech> getSpeeches();

    /**
     * Add a speech to this agenda
     * @param pValue
     */
    void addSpeech(Speech pValue);



    /**
     * Return the index of the agenda
     * @return
     */
    String getIndex();

    /**
     * Set the index of the agenda
     * @param pValue
     */
    void setIndex(String pValue);

    /**
     * Get the title of the agenda
     * @return
     */
    String getTitle();

    /**
     * Set the title of the agenda
     * @param sValue
     */
    void setTitle(String sValue);

    /**
     * Return the protocol to which the agenda item belongs.
     * @return
     */
    PlenaryProtocol getProtocol();

}
