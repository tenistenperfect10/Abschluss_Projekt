package org.texttechnology.parliament_browser_6_4.data.Impl.file;

import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.PlenaryObject;


/**
 * Implementation of the plenary object
 * @author
 */
public class PlenaryObject_File_Impl implements PlenaryObject {

    // Variables declarations
    private String sID = "";
    private int iWahlperiode = -1;

    //ParlamentaryFactory, all inheriting classes can also access it directly.
    protected InsightFactory pFactory = null;

    /**
     * Construcot
     * @param pFactory
     */
    public PlenaryObject_File_Impl(InsightFactory pFactory){
        this.pFactory = pFactory;
    }

    public PlenaryObject_File_Impl(){

    }

    @Override
    public String getID() {
        return this.sID;
    }

    @Override
    public void setID(String lID) {
        this.sID = lID;
    }

    @Override
    public int getWahlperiode() {
        return this.iWahlperiode;
    }

    @Override
    public void setWahlperiode(int iValue) {
        this.iWahlperiode=iValue;
    }

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
