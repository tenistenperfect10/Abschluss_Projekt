package org.texttechnology.parliament_browser_6_4.data.Impl.file;


import org.texttechnology.parliament_browser_6_4.data.Comment;
import org.w3c.dom.Node;

/**
 * Comment implementation
 * @author Giuseppe Abrami
 */
public class Comment_File_Impl extends Text_File_Impl implements Comment {

    /**
     * Constructor based on a Node, calling the super-constructor
     * @param pNode
     */
    public Comment_File_Impl(Node pNode){
        super(pNode.getTextContent());
    }

    /**
     * well, the hash function
     * @return
     */
    @Override
    public int hashCode() {
        return this.getContent().hashCode();
    }
}
