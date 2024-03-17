package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.jetbrains.annotations.NotNull;
import org.texttechnology.parliament_browser_6_4.data.*;
import org.texttechnology.parliament_browser_6_4.helper.XMLHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Speech_Impl extends PlenaryObject_Impl implements Speech {

    private AgendaItem pAgenda = null;
    private List<Comment> textContent = new ArrayList<>();

    private List<Speech> pInsertions = new ArrayList<>();

    private Speaker pSpeaker = null;

    /**
     * Construktor
     * @param pFactory
     */
    public Speech_Impl(InsightFactory pFactory) {
        super(pFactory);
    }

    /**
     * Construktor
     * @param pAgenda
     * @param pNode
     */
    public Speech_Impl(AgendaItem pAgenda, Node pNode) {
        super(pAgenda.getProtocol().getFactory());
        this.pAgenda = pAgenda;
        this.pAgenda.addSpeech(this);
        init(pNode);

    }

    /**
     * Constructor
     * @param pAgenda
     * @param sID
     */
    public Speech_Impl(AgendaItem pAgenda, String sID) {
        super(pAgenda.getProtocol().getFactory());
        this.pAgenda = pAgenda;
        this.setID(sID);
    }

    /**
     * Initialization based on an XML-node
     * @param pNode
     */
    private void init(Node pNode) {
        this.setID(pNode.getAttributes().getNamedItem("id").getTextContent());

        // get Redner

        NodeList nL = pNode.getChildNodes();

        Speaker currentSpeaker = null;
        Speech currentSpeech = this;

        int optionalSpeech = 1;

        for (int a = 0; a < nL.getLength(); a++) {
            Node n = nL.item(a);

            switch (n.getNodeName()) {

                case "p":

                    String sKlasse = "";
                    if (n.hasAttributes()) {
                        sKlasse = n.getAttributes().getNamedItem("klasse").getTextContent();
                    }

                    switch (sKlasse) {
                        case "redner":
                            //Speaker nSpeaker = this.getFactory().getSpeaker(XMLHelper.getSingleNodesFromXML(n, "redner"));
                            //currentSpeaker = nSpeaker;
                            //currentSpeech = this;
                            //nSpeaker.addSpeech(currentSpeech);
                            //this.pSpeaker = nSpeaker;

                            break;
                        case "n":

                            //Speaker tSpeaker = this.getFactory().getSpeaker(n);
                            //currentSpeaker = tSpeaker;
                            //tSpeaker.addSpeech(currentSpeech);

                            break;

                    }

                    break;

                case "name":

                    //Speaker nSpeaker = this.getFactory().getSpeaker(n);
                   // if (nSpeaker == this.getSpeaker()) {
                     //   currentSpeech = this;
                    //} else {
                      //  if (currentSpeaker != nSpeaker && nSpeaker != null) {
                        //    currentSpeaker = nSpeaker;
                          //  currentSpeech = new Speech_Impl(getAgendaItem(), getID() + "-" + optionalSpeech);
                            //currentSpeaker.addSpeech(currentSpeech);
                            //currentSpeech.setSpeaker(currentSpeaker);
                            //pInsertions.add(currentSpeech);
                            //optionalSpeech++;
                        //}
                    //}

                    break;

                case "kommentar":
                    Comment_Impl pComment = new Comment_Impl(n);
                    pComment.setSpeech(this);
                    pComment.setSpeaker(this.getSpeaker());
                    textContent.add(pComment);
                    break;

            }

        }


    }




    @Override
    public AgendaItem getAgendaItem() {
        return this.pAgenda;
    }

    /**
     * Return all comments in this speech
     *
     * @return
     */
    @Override
    public List<Comment> getComments() {
        List<Comment> rList = new ArrayList<>();
        this.textContent.stream().filter(c -> c instanceof Comment).forEach(c -> {
            rList.add((Comment) c);
        });
        return rList;
    }

    /**
     * Return the full text of the speech
     *
     * @return
     */
    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        textContent.forEach(t -> {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (t instanceof Comment) {
                sb.append("\n\n");
                sb.append("\t" + t.getContent());
                sb.append("\n\n");
            } else {
                sb.append(t.getContent());
            }


        });
        return sb.toString();
    }

    @Override
    public String getPlainText() {
        StringBuilder sb = new StringBuilder();

        textContent.forEach(t->{
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if(!(t instanceof Comment)){
                sb.append(t.getContent());
            }
        });

        return sb.toString();
    }

    /**
     * Return the plenary protocol this speechs belongs to
     *
     * @return
     */
    @Override
    public PlenaryProtocol getProtocol() {
        return this.getAgendaItem().getProtocol();
    }

    /**
     * Return the speaker of tis speech
     *
     * @return
     */
    @Override
    public Speaker getSpeaker() {
        return this.pSpeaker;
    }

    /**
     * Set the speaker of this speech
     *
     * @param pSpeaker
     */
    @Override
    public void setSpeaker(Speaker pSpeaker) {
        this.pSpeaker = pSpeaker;
    }

    /**
     * Return the length of this speech
     *
     * @return
     */
    @Override
    public int getLength() {
        return getText().length();
    }

    /**
     * Return all insertions which are no comments
     *
     * @return
     */
    @Override
    public List<Speech> getInsertions() {
        return pInsertions;
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
