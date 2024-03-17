package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.jetbrains.annotations.NotNull;
import org.texttechnology.parliament_browser_6_4.data.*;
import org.texttechnology.parliament_browser_6_4.exception.InputException;
import org.texttechnology.parliament_browser_6_4.helper.StringHelper;
import org.texttechnology.parliament_browser_6_4.helper.XMLHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class PlenaryProtocol_Impl extends PlenaryObject_Impl implements PlenaryProtocol {

    // The DOM document is stored
    private Document pDocument = null;

    // variable declaration
    private int iIndex = -1;
    private Date pDate = null;
    private Timestamp pStartTime = null;
    private Timestamp pEndTime = null;
    private String sTitle = "";
    private String sPlace = "";

    private List<AgendaItem> pAgendaItems = new ArrayList<>(0);

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");

    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    public PlenaryProtocol_Impl(InsightFactory pFactory){
        super(pFactory);
    }

    /**
     * Constuctor. The constructor needs a ParliamentFactory and the file of the plenary protocol
     * @param pFactory
     * @param pFile
     */
    public PlenaryProtocol_Impl(InsightFactory pFactory, File pFile){
        super(pFactory);
        try {
            init(pFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialization based on a file
     * @param pFile
     */
    private void init(File pFile) throws ParserConfigurationException, IOException, SAXException, ParseException {

        // Define Date and Time Format
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        // create a document builder factory for the parsing of XML documents
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        // parse XML file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document pDocument = db.parse(pFile);
        this.pDocument=pDocument;

        /**
         * Extract information of the header of the file, by use of a helper method
         */
        Node nWahlperiode = getNodeFromXML(pDocument, "wahlperiode");
        this.setWahlperiode(Integer.valueOf(nWahlperiode.getTextContent()));

        Node nSitzungsnummer = getNodeFromXML(pDocument, "sitzungsnr");
        int iSitzungsnummer = -1;

        try {
            iSitzungsnummer = Integer.valueOf(nSitzungsnummer.getTextContent());
        }
        catch (NumberFormatException nfe){
            iSitzungsnummer = Integer.valueOf(nSitzungsnummer.getTextContent().split(" ")[0]);
        }

        this.setIndex(iSitzungsnummer);

        Node nTitle = getNodeFromXML(pDocument, "plenarprotokoll-nummer");
        this.setTitle(nTitle.getTextContent());

        Node nOrt = getNodeFromXML(pDocument, "ort");
        this.setPlace(nOrt.getTextContent());

        Node nDatum = getNodeFromXML(pDocument, "datum");
        Date pDate = new Date(sdfDate.parse(nDatum.getAttributes().getNamedItem("date").getTextContent()).getTime());
        this.setDate(pDate);

        // extract the start of a session
        Node nStart = getNodeFromXML(pDocument, "sitzungsbeginn");
        Time pStartTime = null;
        String sStartTime = nStart.getAttributes().getNamedItem("sitzung-start-uhrzeit").getTextContent();
        sStartTime = sStartTime.replaceAll("\\.", ":");
        sStartTime = sStartTime.replace(" Uhr", "");
        try {
            pStartTime = new Time(sdfTime.parse(sStartTime).getTime());
        }
        catch (ParseException pe){
            pStartTime = new Time(sdfTime.parse(nStart.getAttributes().getNamedItem("sitzung-start-uhrzeit").getTextContent().replaceAll("\\.", ":")).getTime());
        }
        this.setStarttime(pStartTime);

        // extract the start of a session
        Node nEnde = getNodeFromXML(pDocument, "sitzungsende");
        String sEndTime = nEnde.getAttributes().getNamedItem("sitzung-ende-uhrzeit").getTextContent();
        sEndTime = sEndTime.replaceAll("\\.", ":");
        sEndTime = sEndTime.replace(" Uhr", "");
        Time pEndTime = null;
        try {
            pEndTime = new Time(sdfTime.parse(sEndTime).getTime());
        }
        catch (ParseException pe){
            try {
                pEndTime = new Time(sdfTime.parse(nEnde.getAttributes().getNamedItem("sitzung-ende-uhrzeit").getTextContent().replaceAll("\\.", ":")).getTime());
            }
            catch (ParseException peFinal){
                System.err.println(peFinal.getMessage());
            }
        }
        if(pEndTime!=null) {
            this.setEndTime(pEndTime);
        }
        else{
            this.setEndTime(pStartTime);
        }

        pStartTime = new Time(this.getDate().getTime()+this.getStartTime().getTime());

        if(this.getEndTime().before(this.getStartTime())){
            Calendar pCalender = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            pCalender.setTime(this.getDate());
            pCalender.add(Calendar.DAY_OF_YEAR, 1);
            pEndTime = new Time(pCalender.getTime().getTime()+this.getEndTime().getTime());
        }
        else{
            pEndTime = new Time(this.getDate().getTime()+this.getEndTime().getTime());
        }

        this.setStarttime(pStartTime);
        this.setEndTime(pEndTime);

        // Processing AgendaIgems
        NodeList pBlocks = pDocument.getElementsByTagName("ivz-block");

        for(int b=0; b<pBlocks.getLength(); b++){
            Node n = pBlocks.item(b);

            AgendaItem pItem = new AgendaItem_Impl(this, n);
            if(pItem.getSpeeches().size()>0){
                this.addAgendaItem(pItem);
            }

        }


    }

    private void addAbsences(NodeList pAnlagen){

        for(int a=0; a<pAnlagen.getLength(); a++){
            Node pNode = XMLHelper.getSingleNodesFromXML(pAnlagen.item(0), "table");

            if(pNode!=null) {

                List<Node> trNodes = XMLHelper.getNodesFromXML(pNode, "tr");

                for (Node trNode : trNodes) {

                    List<Node> tNodes = XMLHelper.getNodesFromXML(trNode, "td");
                    for (Node tNode : tNodes) {

                        String sContent = tNode.getTextContent();
                        sContent = sContent.replaceAll("\\*", "");
                        if (sContent.contains(", ")) {
                            String[] sNames = sContent.split(", ");
                            String lastName = sNames[0];
                            String firstName = sNames[1];

                            firstName = StringHelper.replaceList(firstName, StringHelper.FIRSTNAME);

                            lastName = StringHelper.replaceList(lastName, StringHelper.REGEXCLEAN);

                            String finalFirstName = firstName;
                            String finalLastName = lastName;
                           // Set<Speaker> speakerSet = this.getFactory().getSpeakers().stream().filter(s -> {
                              //  return s.getName().equalsIgnoreCase(finalLastName) && s.getFirstName().equalsIgnoreCase(finalFirstName);
                            //}).collect(Collectors.toSet());

                           // speakerSet.forEach(pSpeaker -> {
                             //   pSpeaker.addAbsense(this);
                            //});


                        }

                    }

                }
            }

        }





    }

    /**
     * Return the XML-document
     * @return
     */
    protected Document getFile(){
        return this.pDocument;
    }

    /**
     * Extract a Node by a tag-name
     * @param pDocument
     * @param sTag
     * @return
     */
    private Node getNodeFromXML(Document pDocument, String sTag){
        return pDocument.getElementsByTagName(sTag).item(0);
    }

    /**
     * Return the sequence number fo the protocol
     *
     * @return
     */
    @Override
    public int getIndex() {
        return 0;
    }

    /**
     * Set the sequence number fo the protocol
     *
     * @param iValue
     */
    @Override
    public void setIndex(int iValue) {

    }

    /**
     * Return the date of the protocol
     *
     * @return
     */
    @Override
    public Date getDate() {
        return null;
    }

    /**
     * Return the formated Date
     *
     * @return
     */
    @Override
    public String getDateFormated() {
        return null;
    }

    /**
     * Set the date of the protocol
     *
     * @param pDate
     */
    @Override
    public void setDate(Date pDate) {

    }

    /**
     * Return the start time of the protocol
     *
     * @return
     */
    @Override
    public Timestamp getStartTime() {
        return null;
    }

    /**
     * Return the start time formated
     *
     * @return
     */
    @Override
    public String getStartTimeFormated() throws InputException {
        return null;
    }

    /**
     * Set the start time of the protocol
     *
     * @param pTime
     */
    @Override
    public void setStarttime(Time pTime) {

    }

    /**
     * Return the end time of the protocol
     *
     * @return
     */
    @Override
    public Timestamp getEndTime() {
        return null;
    }

    /**
     * Return the end time formated
     *
     * @return
     */
    @Override
    public String getEndTimeFormated() throws InputException {
        return null;
    }

    /**
     * Set the end time of the protocol
     *
     * @param pTime
     */
    @Override
    public void setEndTime(Time pTime) {

    }

    /**
     * Return the title of the protocol
     *
     * @return
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * Set the title of the protocol
     *
     * @param sValue
     */
    @Override
    public void setTitle(String sValue) {

    }

    /**
     * Return the agenda items of the protocol
     *
     * @return
     */
    @Override
    public List<AgendaItem> getAgendaItems() {
        return null;
    }

    /**
     * Add a agenda item
     *
     * @param pItem
     */
    @Override
    public void addAgendaItem(AgendaItem pItem) {

    }

    /**
     * Add multiple agenda items
     *
     * @param pSet
     */
    @Override
    public void addAgendaItems(Set<AgendaItem> pSet) {

    }

    /**
     * Return the place of the protocol
     *
     * @return
     */
    @Override
    public String getPlace() {
        return null;
    }

    /**
     * Set the place of the protocol
     *
     * @param sValue
     */
    @Override
    public void setPlace(String sValue) {

    }


    /**
     * Return a list of speakers of the protocol
     *
     * @return
     */
    @Override
    public Set<Speaker> getSpeakers() {
        Set<Speaker> rSet = new HashSet<>(0);

        getAgendaItems().forEach(ai->{
            ai.getSpeeches().forEach(speech -> {
                rSet.add(speech.getSpeaker());
            });
        });

        return rSet;
    }

    @Override
    public String toString() {
        return this.getIndex()+"\t"+this.getDateFormated()+"\t"+this.getPlace();
    }

    @Override
    public boolean equals(Object o) {
        return this.hashCode()==o.hashCode();
    }

    /**
     * Special hashCode method
     * @return
     */
    @Override
    public int hashCode() {
        return this.getIndex();
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
     * @param plenaryObject the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(PlenaryObject plenaryObject) {
        if(plenaryObject instanceof PlenaryProtocol){
            return ((PlenaryProtocol)plenaryObject).getDate().compareTo(this.getDate());
        }
        return super.compareTo(plenaryObject);
    }
}
