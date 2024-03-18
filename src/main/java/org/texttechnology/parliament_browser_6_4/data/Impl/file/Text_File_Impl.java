package org.texttechnology.parliament_browser_6_4.data.Impl.file;


import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.Speaker;
import org.texttechnology.parliament_browser_6_4.data.Speech;
import org.texttechnology.parliament_browser_6_4.data.Text;

/**
 * Implementation of a text segment of a speech.
 * @author
 */
public class Text_File_Impl extends PlenaryObject_File_Impl implements Text {

    private Speaker pSpeaker = null;
    private Speech pSpeech = null;
    private String sText = "";

    public Text_File_Impl(InsightFactory pFactory){
        super(pFactory);
    }

    /**
     * Constructor
     * @param pSpeaker
     * @param pSpeech
     * @param sText
     */
    public Text_File_Impl(Speaker pSpeaker, Speech pSpeech, String sText){
        this.pSpeaker = pSpeaker;
        this.pSpeech = pSpeech;
        this.sText = sText;

    }

    public Text_File_Impl(String sText){
        this.sText = sText;
    }

    @Override
    public Speech getSpeech() {
        return this.pSpeech;
    }

    @Override
    public Speaker getSpeaker() {
        return this.pSpeaker;
    }

    @Override
    public void setSpeech(Speech pSpeech) {
        this.pSpeech = pSpeech;
    }

    @Override
    public void setSpeaker(Speaker pSpeaker) {
        this.pSpeaker = pSpeaker;
    }

    @Override
    public String getContent() {
        return this.sText;
    }

    @Override
    public String getID() {
        return this.getSpeech().getID()+"-"+this.getContent().hashCode();
    }
}
