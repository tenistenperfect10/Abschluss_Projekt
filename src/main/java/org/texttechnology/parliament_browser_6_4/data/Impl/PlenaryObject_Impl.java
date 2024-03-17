package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.jetbrains.annotations.NotNull;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.PlenaryObject;

public class PlenaryObject_Impl implements PlenaryObject {

    private String sID = "";
    private int iWahlperiode = -1;

    //ParlamentaryFactory, all inheriting classes can also access it directly.
    protected InsightFactory pFactory = null;

    /**
     * Construcot
     * @param pFactory
     */
    public PlenaryObject_Impl(InsightFactory pFactory){
        this.pFactory = pFactory;
    }

    public PlenaryObject_Impl(){

    }

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
        return this.pFactory;
    }

    @Override
    public int compareTo(PlenaryObject plenaryObject) {
        return getID().compareTo(plenaryObject.getID());
    }

    @Override
    public boolean equals(Object o) {
        return o.hashCode()==this.hashCode();
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }
}
