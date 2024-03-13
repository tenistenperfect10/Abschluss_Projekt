package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.Member;
import org.texttechnology.parliament_browser_6_4.data.MemberImg;

import java.util.List;

public class Member_Impl implements Member {

    // 主键id
    private String id;

    // 姓
    private String nachName;

    // 名
    private String vorName;

    // 地点补充
    private String ortszusatz;

    // 称谓头衔
    private String anredeTitle;

    // 学术头衔
    private String akadTitle;

    // 任职开始
    private String historieVon;

    // 任职结束
    private String historieBis;

    private List<MemberImg> memberImgList;



    @Override
    public String getNachName() {
        return this.nachName;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;

    }

    @Override
    public void setNachName(String nachName) {

        this.nachName = nachName;

    }

    @Override
    public String getVorName() {

        return this.vorName;
    }

    @Override
    public void setVorName(String vorName) {

        this.vorName = vorName;

    }

    @Override
    public String getOrtszusatz() {
        return this.ortszusatz;
    }

    @Override
    public void setOrtszusatz(String ortszusatz) {

        this.ortszusatz = ortszusatz;

    }

    @Override
    public String getAnredeTitle() {

        return this.anredeTitle;
    }

    @Override
    public void setAnredeTitle(String anredeTitle) {

        this.anredeTitle = anredeTitle;

    }

    @Override
    public String getAkadTitle() {

        return this.akadTitle;
    }

    @Override
    public void setAkadTitle(String akadTitle) {

        this.akadTitle = akadTitle;

    }

    @Override
    public String getHistorieVon() {

        return this.historieVon;
    }

    @Override
    public void setHistorieVon(String historieVon) {

        this.historieVon = historieVon;

    }

    @Override
    public String getHistorieBis() {

        return this.historieBis;
    }

    @Override
    public void setHistorieBis(String historieBis) {

        this.historieBis = historieBis;

    }

    @Override
    public List<MemberImg> getMemberImgList() {

        return this.memberImgList;
    }

    @Override
    public void setMemberImgList(List<MemberImg> memberImgList) {

        this.memberImgList = memberImgList;

    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", nachname='" + nachName + '\'' +
                ", vorname='" + vorName + '\'' +
                ", ortszusatz='" + ortszusatz + '\'' +
                ", anredetitle='" + anredeTitle + '\'' +
                ", akadtitle='" + akadTitle + '\'' +
                ", historievon='" + historieVon + '\'' +
                ", historiebis='" + historieBis + '\'' +
                ", memberImgList=" + memberImgList +
                '}';
    }
}
