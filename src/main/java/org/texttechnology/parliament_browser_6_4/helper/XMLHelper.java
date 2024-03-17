package org.texttechnology.parliament_browser_6_4.helper;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class XMLHelper {

    /**
     * List all nodes based on a tag-name as part of a given node
     * @param pNode
     * @param sNodeName
     * @return
     */
    public static List<Node> getNodesFromXML(Node pNode, String sNodeName){

        List<Node> rSet = new ArrayList<>(0);

        if(pNode.getNodeName().equals(sNodeName)) {
            rSet.add(pNode);

        }
        else{

            if (pNode.hasChildNodes()) {
                for (int a = 0; a < pNode.getChildNodes().getLength(); a++) {
                    rSet.addAll(getNodesFromXML(pNode.getChildNodes().item(a), sNodeName));
                }
            } else {
                if (pNode.getNodeName().equals(sNodeName)) {
                    rSet.add(pNode);
                }
            }
        }

        return rSet;

    }

    /**
     * Get one node on a tag-name as part of a given node
     * @param pNode
     * @param sNodeName
     * @return
     */
    public static Node getSingleNodesFromXML(Node pNode, String sNodeName){

        List<Node> nList = getNodesFromXML(pNode, sNodeName);

        if(nList.size()>0){
            return nList.stream().findFirst().get();
        }
        return null;

    }


}
