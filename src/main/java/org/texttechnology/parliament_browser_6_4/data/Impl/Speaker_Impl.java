package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.jetbrains.annotations.NotNull;
import org.texttechnology.parliament_browser_6_4.data.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Speaker_Impl extends PlenaryObject_Impl implements Speaker {

    // Speaker variables
    protected String sName = "";
    protected String sFirstName = "";
    protected String sAdressing = "";
    protected String sTitle = "";
    protected String sRole = "";

    protected Set<Speech> pSpeeches = new HashSet<>();

    protected Set<PlenaryProtocol> pAbsences = new HashSet<>(0);

    protected Fraction pFraction = null;
    protected Party pParty = null;

    protected String sBeruf = "";
    protected String sReligion = "";
    protected String sFamilienstand = "";
    protected String sGeburtsort = "";

    protected String sVita = "";

    protected Date pGeburtsdatum = null;
    protected String sAkademischerTitel = "";
    protected Types.GESCHLECHT pGeschlecht = Types.GESCHLECHT.MAENNLICH;
    protected Date pSterbedatum = null;


    /**
     * Return the ID of the Object
     *
     * @return
     */
    @Override
    public String getID() {
        return null;
    }

    /**
     * Set the ID of the Object
     *
     * @param sID
     */
    @Override
    public void setID(String sID) {

    }

    /**
     * Get the Wahlperiode
     *
     * @return
     */
    @Override
    public int getWahlperiode() {
        return 0;
    }

    /**
     * Set the Wahlperiode
     *
     * @param iValue
     */
    @Override
    public void setWahlperiode(int iValue) {

    }

    /**
     * Return the ParliamentFactory
     *
     * @return
     */
    @Override
    public InsightFactory getFactory() {
        return null;
    }

    /**
     * Return the party of the speaker
     *
     * @return
     */
    @Override
    public Party getParty() {
        return null;
    }

    /**
     * Set the party of the speaker
     *
     * @param pParty
     */
    @Override
    public void setParty(Party pParty) {

    }

    /**
     * Return the fraction of the speaker
     *
     * @return
     */
    @Override
    public Fraction getFraction() {
        return null;
    }

    /**
     * Set the fraction of the speaker
     *
     * @param pFraction
     */
    @Override
    public void setFraction(Fraction pFraction) {

    }

    /**
     * Return the role of the speaker
     *
     * @return
     */
    @Override
    public String getRole() {
        return null;
    }

    /**
     * Set the role of the speaker
     *
     * @param sValue
     */
    @Override
    public void setRole(String sValue) {

    }

    /**
     * Return the title of the speaker
     *
     * @return
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * Set the title of the speaker
     *
     * @param sValue
     */
    @Override
    public void setTitle(String sValue) {

    }

    /**
     * Return the surname of the speaker
     *
     * @return
     */
    @Override
    public String getName() {
        return null;
    }

    /**
     * Set the name of the speaker
     *
     * @param sValue
     */
    @Override
    public void setName(String sValue) {

    }

    /**
     * Return the firstname of the speaker
     *
     * @return
     */
    @Override
    public String getFirstName() {
        return null;
    }

    /**
     * Set the firstname of the speaker
     *
     * @param sValue
     */
    @Override
    public void setFirstName(String sValue) {

    }

    /**
     * Return all speeches of the speaker
     *
     * @return
     */
    @Override
    public Set<Speech> getSpeeches() {
        return null;
    }

    /**
     * Add a speech to the speaker
     *
     * @param pSpeech
     */
    @Override
    public void addSpeech(Speech pSpeech) {

    }

    /**
     * Add multiple speeches to the speaker
     *
     * @param pSet
     */
    @Override
    public void addSpeeches(Set<Speech> pSet) {

    }

    /**
     * Return all comments the speaker received
     *
     * @return
     */
    @Override
    public Set<Comment> getComments() {
        return null;
    }

    /**
     * Return the avg length of all speeches
     *
     * @return
     */
    @Override
    public float getAvgLength() {
        return 0;
    }

    /**
     * Return the Sessions where a speaker was absence
     *
     * @return
     */
    @Override
    public Set<PlenaryProtocol> getAbsences() {
        return null;
    }

    /**
     * Get the Quote of Absence by Wahlperiode
     *
     * @param iWP
     * @return
     */
    @Override
    public float getAbsences(int iWP) {
        return 0;
    }

    /**
     * Add an absence to a speaker
     *
     * @param pProtocol
     */
    @Override
    public void addAbsense(PlenaryProtocol pProtocol) {

    }

    @Override
    public String getAkademischerTitel() {
        return null;
    }

    @Override
    public void setAkademischerTitel(String sValue) {

    }

    @Override
    public Date getGeburtsdatum() {
        return null;
    }

    @Override
    public void setGeburtsdatum(Date pDate) {

    }

    @Override
    public String getGeburtsort() {
        return null;
    }

    @Override
    public void setGeburtsort(String sValue) {

    }

    @Override
    public String getFamilienstand() {
        return null;
    }

    @Override
    public void setFamilienstand(String sValue) {

    }

    @Override
    public String getReligion() {
        return null;
    }

    @Override
    public void setReligion(String sValue) {

    }

    @Override
    public String getBeruf() {
        return null;
    }

    @Override
    public void setBeruf(String sValue) {

    }

    @Override
    public void setGeschlecht(Types.GESCHLECHT pValue) {

    }

    @Override
    public Types.GESCHLECHT getGeschlecht() {
        return null;
    }

    @Override
    public void setSterbeDatum(Date pValue) {

    }

    @Override
    public Date getSterbeDatum() {
        return null;
    }

    @Override
    public String getAdressing() {
        return null;
    }

    @Override
    public void setAdressing(String textContent) {

    }

    @Override
    public void setVita(String textContent) {

    }

    @Override
    public String getVita() {
        return null;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(@NotNull PlenaryObject o) {
        return 0;
    }
}
