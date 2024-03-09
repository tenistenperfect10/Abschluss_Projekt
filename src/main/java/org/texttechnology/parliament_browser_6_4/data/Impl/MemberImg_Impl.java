package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.MemberImg;

public class MemberImg_Impl implements MemberImg {

    private String imgLink;

    private String description;

    public MemberImg_Impl(String imgLink, String description) {
        this.imgLink = imgLink;
        this.description = description;
    }


    @Override
    public String getImgLink() {
        return this.imgLink;
    }

    @Override
    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;

    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {

        this.description = description;

    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "imgLink='" + imgLink + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
