package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.jetbrains.annotations.NotNull;
import org.texttechnology.parliament_browser_6_4.data.*;
import org.texttechnology.parliament_browser_6_4.helper.XMLHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AgendaItem_Impl extends PlenaryObject_Impl implements AgendaItem {

    private PlenaryProtocol pProtocol = null;
    private String sIndex = "";
    private String sTitle = "";
    private List<Speech> pSpeeches = new ArrayList<>(0);

    /**
     * Constructor
     * @param pFactory
     */
    public AgendaItem_Impl(InsightFactory pFactory){
        super(pFactory);
    }


    /**
     * Constructor
     * @param pProtocol
     * @param n
     */
    public AgendaItem_Impl(PlenaryProtocol pProtocol, Node n){
        super(pProtocol.getFactory());
        this.pProtocol = pProtocol;
        init(n);
    }

    /**
     * Initialization based on a node
     * @param n
     */
    private void init(Node n){

        // Reading the table of contents
        List<Node> pNodes = XMLHelper.getNodesFromXML(n, "ivz-block-titel");

        if(pNodes.size()==1){
            Node current = pNodes.stream().findFirst().get();

            this.setIndex(current.getTextContent().replace(":", ""));

            List<Node> contentNodes = XMLHelper.getNodesFromXML(current.getParentNode(), "ivz-eintrag-inhalt");
            if(contentNodes.size()>0) {

                StringBuilder sb = new StringBuilder();

                for (Node contentNode : contentNodes) {
                    if(current.getNodeType()==Node.TEXT_NODE) {
                        if (sb.length() > 0) {
                            sb.append("\n");
                        }
                        sb.append(contentNode.getTextContent());
                    }
                }

                this.setTitle(sb.toString());
            }

        }

        // Query of the speeches in the agenda item
        NodeList nl = ((PlenaryProtocol_Impl)getProtocol()).getFile().getElementsByTagName("tagesordnungspunkt");

        for(int a=0; a<nl.getLength(); a++){
            Node top = nl.item(a);
            if(top.getAttributes().getNamedItem("top-id").getTextContent().equals(this.getIndex())){
                List<Node> pRede = XMLHelper.getNodesFromXML(top, "rede");

                // Create a speech for each speech in the agenda
                pRede.forEach(r->{
                    Speech pSpeech = new Speech_Impl(this, r);
                });

            }
        }

    }



    /**
     * Get all Speeches
     *
     * @return
     */
    @Override
    public List<Speech> getSpeeches() {
        return this.pSpeeches;
    }

    /**
     * Add a speech to this agenda
     *
     * @param pValue
     */
    @Override
    public void addSpeech(Speech pValue) {

        this.pSpeeches.add(pValue);

    }


    /**
     * Return the index of the agenda
     *
     * @return
     */
    @Override
    public String getIndex() {
        return this.sIndex;
    }

    /**
     * Set the index of the agenda
     *
     * @param pValue
     */
    @Override
    public void setIndex(String pValue) {
        this.sIndex = pValue;
    }

    /**
     * Get the title of the agenda
     *
     * @return
     */
    @Override
    public String getTitle() {
        return this.sTitle;
    }

    /**
     * Set the title of the agenda
     *
     * @param sValue
     */
    @Override
    public void setTitle(String sValue) {
        this.sTitle = sValue;

    }

    /**
     * Return the protocol to which the agenda item belongs.
     *
     * @return
     */
    @Override
    public PlenaryProtocol getProtocol() {
        return this.pProtocol;
    }


    @Override
    public String toString() {
        return getProtocol().getIndex()+"\t"+getIndex()+"\t"+getTitle();
    }

    @Override
    public boolean equals(Object o) {
        return hashCode()==o.hashCode();
    }

    @Override
    public int hashCode() {
        return this.getIndex().hashCode();
    }
}
