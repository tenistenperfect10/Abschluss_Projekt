
package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.Member;
import org.texttechnology.parliament_browser_6_4.data.MemberImg;

import java.util.List;

/**
 * Implementation of the Member interface, representing a parliamentary member.
 * It includes details such as names, titles, and service history.
 */
public class Member_Impl implements Member {

    // Primary key identifier
    private String id;

    // Last name
    private String nachName;

    // First name
    private String vorName;

    // Additional location information
    private String ortszusatz;

    // Formal title or salutation
    private String anredeTitle;

    // Academic title
    private String akadTitle;

    // Service start date
    private String historieVon;

    // Service end date
    private String historieBis;

    // List of member images
    private List<MemberImg> memberImgList;

    /**
     * Returns the last name of the member.
     * @return The last name.
     */
    @Override
    public String getNachName() {
        return this.nachName;
    }

    /**
     * Returns the member's ID.
     * @return The ID.
     */
    @Override
    public String getId() {
        return this.id;
    }

    /**
     * Sets the member's ID.
     * @param id The new ID.
     */
    @Override
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the last name of the member.
     * @param nachName The new last name.
     */
    @Override
    public void setNachName(String nachName) {
        this.nachName = nachName;
    }

    /**
     * Returns the first name of the member.
     * @return The first name.
     */
    @Override
    public String getVorName() {
        return this.vorName;
    }

    /**
     * Sets the first name of the member.
     * @param vorName The new first name.
     */
    @Override
    public void setVorName(String vorName) {
        this.vorName = vorName;
    }

    /**
     * Returns additional location information.
     * @return The location supplement.
     */
    @Override
    public String getOrtszusatz() {
        return this.ortszusatz;
    }

    /**
     * Sets additional location information.
     * @param ortszusatz The new location supplement.
     */
    @Override
    public void setOrtszusatz(String ortszusatz) {
        this.ortszusatz = ortszusatz;
    }

    /**
     * Returns the formal title or salutation of the member.
     * @return The title or salutation.
     */
    @Override
    public String getAnredeTitle() {
        return this.anredeTitle;
    }

    /**
     * Sets the formal title or salutation of the member.
     * @param anredeTitle The new title or salutation.
     */
    @Override
    public void setAnredeTitle(String anredeTitle) {
        this.anredeTitle = anredeTitle;
    }

    /**
     * Returns the academic title of the member.
     * @return The academic title.
     */
    @Override
    public String getAkadTitle() {
        return this.akadTitle;
    }

    /**
     * Sets the academic title of the member.
     * @param akadTitle The new academic title.
     */
    @Override
    public void setAkadTitle(String akadTitle) {
        this.akadTitle = akadTitle;
    }

    /**
     * Returns the service start date.
     * @return The start date.
     */
    @Override
    public String getHistorieVon() {
        return this.historieVon;
    }

    /**
     * Sets the service start date.
     * @param historieVon The new start date.
     */
    @Override
    public void setHistorieVon(String historieVon) {
        this.historieVon = historieVon;
    }

    /**
     * Returns the service end date.
     * @return The end date.
     */
    @Override
    public String getHistorieBis() {
        return this.historieBis;
    }

    /**
     * Sets the service end date.
     * @param

    historieBis The new end date.
     */
    @Override
    public void setHistorieBis(String historieBis) {
        this.historieBis = historieBis;
    }

    /**
     * Returns the list of images associated with the member.
     * @return The list of MemberImg objects.
     */
    @Override
    public List<MemberImg> getMemberImgList() {
        return this.memberImgList;
    }

    /**
     * Sets the list of images associated with the member.
     * @param memberImgList The new list of MemberImg objects.
     */
    @Override
    public void setMemberImgList(List<MemberImg> memberImgList) {
        this.memberImgList = memberImgList;
    }

    /**
     * Returns a string representation of the member, including all attributes.
     * @return A string representation of the Member object.
     */
    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", nachName='" + nachName + '\'' +
                ", vorName='" + vorName + '\'' +
                ", ortszusatz='" + ortszusatz + '\'' +
                ", anredeTitle='" + anredeTitle + '\'' +
                ", akadTitle='" + akadTitle + '\'' +
                ", historieVon='" + historieVon + '\'' +
                ", historieBis='" + historieBis + '\'' +
                ", memberImgList=" + memberImgList +
                '}';
    }
}
