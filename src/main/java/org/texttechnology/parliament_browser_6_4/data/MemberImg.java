package org.texttechnology.parliament_browser_6_4.data;

/**
 * Interface defining the contract for an image of a Member entity within a parliamentary system.
 * It specifies the methods for managing the image link and a description of the image.
 */
public interface MemberImg {

    /**
     * Returns the link to the image of the member.
     * @return The image link as a String.
     */
    String getImgLink();

    /**
     * Sets the link for the member's image.
     * @param imgLink The new image link.
     */
    void setImgLink(String imgLink);

    /**
     * Returns the description of the member's image.
     * @return The image description.
     */
    String getDescription();

    /**
     * Sets the description for the member's image.
     * @param description The new image description.
     */
    void setDescription(String description);
}
