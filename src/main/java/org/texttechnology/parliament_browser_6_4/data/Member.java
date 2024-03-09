package org.texttechnology.parliament_browser_6_4.data;
import java.util.List;

public interface Member {

    String getNachName();

    String getId();

    void setId(String id);

    void setNachName(String nachName);

    String getVorName();

    void setVorName(String vorName);

    String getOrtszusatz();

    void setOrtszusatz(String ortszusatz);

    String getAnredeTitle();

    void setAnredeTitle(String anredeTitle);

    String getAkadTitle();

    void setAkadTitle(String akadTitle);

    String getHistorieVon();

    void setHistorieVon(String historieVon);

    String getHistorieBis();

    void setHistorieBis(String historieBis);

    List<MemberImg> getMemberImgList();

    void setMemberImgList(List<MemberImg> memberImgList);

}
