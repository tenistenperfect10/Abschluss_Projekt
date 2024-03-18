package org.texttechnology.parliament_browser_6_4.data.Impl.file;

import org.texttechnology.parliament_browser_6_4.data.InsightFactory;


/**
 * Special speaker implementation, for all speakers that are not complex nodes.
 * @author
 */
public class Speaker_Plain_File_Impl extends Speaker_File_Impl {

    /**
     * Constructor
     * @param pFactory
     */
    public Speaker_Plain_File_Impl(InsightFactory pFactory) {
        super(pFactory);
    }

    @Override
    public void setName(String sValue) {
        super.setName(transform(sValue));
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean isLeader() {
        return super.isLeader();
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * Transform-Method for some "errors" in the data
     * @param sValue
     * @return
     */
    public static String transform(String sValue){

        String sReturn = sValue;

        sReturn = sReturn.replaceAll("Vizepräsident in", "Vizepräsidentin");
        sReturn = sReturn.replaceAll("Vizepräsiden t", "Vizepräsident");
        sReturn = sReturn.replaceAll(":", "");

        return sReturn;

    }
}
