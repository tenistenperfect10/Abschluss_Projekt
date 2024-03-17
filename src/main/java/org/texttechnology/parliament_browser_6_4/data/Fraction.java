package org.texttechnology.parliament_browser_6_4.data;

import java.util.Set;

public interface Fraction {

    /**
     * Return the Name of the Fraction
     * @return
     */
    String getName();

    /**
     * Add a Member of the Fraction
     * @param pSpeaker
     */
    void addMember(Speaker pSpeaker);

    /**
     * Get all Members of the Fraction
     * @return
     */
    Set<Speaker> getMembers();

}
