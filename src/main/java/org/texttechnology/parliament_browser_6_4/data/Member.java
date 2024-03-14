package org.texttechnology.parliament_browser_6_4.data;

import java.util.List;

/**
 * Interface defining the contract for a Member entity within a parliamentary system.
 * It outlines the basic properties and functions necessary to manage a member's information,
 * including personal details, titles, and parliamentary service history.
 */
public interface Member {

    /**
     * Returns the last name of the member.
     * @return The last name.
     */
    String getNachName();

    /**
     * Returns the unique identifier of the member.
     * @return The member's ID.
     */
    String getId();

    /**
     * Sets the unique identifier for the member.
     * @param id The new ID.
     */
    void setId(String id);

    /**
     * Sets the last name of the member.
     * @param nachName The new last name.
     */
    void setNachName(String nachName);

    /**
     * Returns the first name of the member.
     * @return The first name.
     */
    String getVorName();

    /**
     * Sets the first name of the member.
     * @param vorName The new first name.
     */
    void setVorName(String vorName);

    /**
     * Returns additional location information associated with the member.
     * @return The location supplement.
     */
    String getOrtszusatz();

    /**
     * Sets additional location information for the member.
     * @param ortszusatz The new location supplement.
     */
    void setOrtszusatz(String ortszusatz);

    /**
     * Returns the formal title or salutation of the member.
     * @return The title or salutation.
     */
    String getAnredeTitle();

    /**
     * Sets the formal title or salutation of the member.
     * @param anredeTitle The new title or salutation.
     */
    void setAnredeTitle(String anredeTitle);

    /**
     * Returns the academic title of the member.
     * @return The academic title.
     */
    String getAkadTitle();

    /**
     * Sets the academic title of the member.
     * @param akadTitle The new academic title.
     */
    void setAkadTitle(String akadTitle);

    /**
     * Returns the service start date for the member.
     * @return The start date.
     */
    String getHistorieVon();

    /**
     * Sets the service start date for the member.
     * @param historieVon The new start date.
     */
    void setHistorieVon(String historieVon);

    /**
     * Returns the service end date for the member.
     * @return The end date.
     */
    String getHistorieBis();

    /**
     * Sets the service end date for the member.
     * @param historieBis The new end date.
     */
    void setHistorieBis(String historieBis);

    /**
     * Returns a list of images associated with the member.
     * @return A list of MemberImg objects.
     */
    List<MemberImg> getMemberImgList();

    /**
     * Sets a list of images associated with the member.
     * @param memberImgList The new list of MemberImg objects.
     */
    void setMemberImgList(List<MemberImg> memberImgList);
}
