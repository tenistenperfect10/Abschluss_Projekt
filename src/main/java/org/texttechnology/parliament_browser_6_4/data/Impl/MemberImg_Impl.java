package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.MemberImg;

/**
 * Implementation of the MemberImg interface, representing an image of a parliamentary member.
 * It includes the image link and a description of the image.
 */
public class MemberImg_Impl implements MemberImg {

    // Image URL
    private String imgLink;

    // Description of the image
    private String description;

    /**
     * Constructs a new MemberImg_Impl object with specified image link and description.
     * @param imgLink The URL link to the image.
     * @param description A textual description of the image.
     */
    public MemberImg_Impl(String imgLink, String description) {
        this.imgLink = imgLink;
        this.description = description;
    }

    /**
     * Returns the link to the image.
     * @return The image link as a String.
     */
    @Override
    public String getImgLink() {
        return this.imgLink;
    }

    /**
     * Sets the link for the image.
     * @param imgLink The new link to set for the image.
     */
    @Override
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    /**
     * Returns the description of the image.
     * @return The image description as a String.
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description for the image.
     * @param description The new description to set for the image.
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a string representation of the MemberImg_Impl object,
     * including its image link and description.
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "MemberInfo{" +
                "imgLink='" + imgLink + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
