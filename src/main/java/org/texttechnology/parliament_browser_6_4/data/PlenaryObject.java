package org.texttechnology.parliament_browser_6_4.data;

public interface PlenaryObject extends Comparable<PlenaryObject>{

    /**
     * Return the ID of the Object
     * @return
     */
    String getID();

    /**
     * Set the ID of the Object
     * @param sID
     */
    void setID(String sID);

    /**
     * Get the Wahlperiode
     * @return
     */
    int getWahlperiode();

    /**
     * Set the Wahlperiode
     * @param iValue
     */
    void setWahlperiode(int iValue);

    /**
     * Return the ParliamentFactory
     * @return
     */
    InsightFactory getFactory();
}
