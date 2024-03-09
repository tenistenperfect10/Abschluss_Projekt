package org.texttechnology.parliament_browser_6_4.task.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.texttechnology.parliament_browser_6_4.data.Impl.MemberImg_Impl;
import org.texttechnology.parliament_browser_6_4.data.Impl.Member_Impl;
import org.texttechnology.parliament_browser_6_4.data.Member;
import org.texttechnology.parliament_browser_6_4.data.MemberImg;
import org.texttechnology.parliament_browser_6_4.helper.HttpRequestUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class MdbParser {

    public static void syncImgToMember(Member member) throws IOException {
        // 通过http请求获取数据
        String firstName = member.getNachName();
        String lastName = member.getVorName();
        String url = String.format("https://bilddatenbank.bundestag.de/search/picture-result?filterQuery[name][]=%s,+%s&sortVal=3", firstName, lastName);
        HttpURLConnection connection = HttpRequestUtils.getBaseConnection(url);
        StringBuilder response = new StringBuilder();
        HttpRequestUtils.handleHttpRequest(connection, response);
        // 解析页面数据，补充Member对象的头像相关信息
        Document doc = Jsoup.parse(response.toString());
        Elements items = doc.select(".rowGridContainer>.item>a");
        List<MemberImg> memberImgs = new ArrayList<>();
        for (org.jsoup.nodes.Element item : items) {
            String description = item.attr("data-caption");
            String imgLink = "https://bilddatenbank.bundestag.de" + item.selectFirst("img").attr("src");
            memberImgs.add(new MemberImg_Impl(imgLink, description));
        }
        member.setMemberImgList(memberImgs);
    }

    public static Member parseMember(Node node) throws IOException {
        Element element = (Element) node;
        String id = element.getElementsByTagName("ID").item(0).getTextContent();

        Element nameElement = (Element) element.getElementsByTagName("NAME").item(0);
        String nachname = nameElement.getElementsByTagName("NACHNAME").item(0).getTextContent();
        String vorname = nameElement.getElementsByTagName("VORNAME").item(0).getTextContent();
        String anredeTitel = nameElement.getElementsByTagName("ANREDE_TITEL").item(0).getTextContent();
        String akadTitel = nameElement.getElementsByTagName("AKAD_TITEL").item(0).getTextContent();
        String historieVon = nameElement.getElementsByTagName("HISTORIE_VON").item(0).getTextContent();
        String historieBis = nameElement.getElementsByTagName("HISTORIE_BIS").item(0).getTextContent();
        String ortsZusatz = nameElement.getElementsByTagName("ORTSZUSATZ").item(0).getTextContent();

        Member_Impl member = new Member_Impl();
        member.setId(id);
        member.setNachName(nachname);
        member.setVorName(vorname);
        member.setAnredeTitle(anredeTitel);
        member.setAkadTitle(akadTitel);
        member.setHistorieVon(historieVon);
        member.setHistorieBis(historieBis);
        member.setOrtszusatz(ortsZusatz);
        syncImgToMember(member);
        return member;
    }
}
