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
/**
 * A parser class for processing and parsing member data from external sources into Member objects,
 * and for syncing image data with these member entities.
 * @author He Liu
 * @author Yingzhu Chen
 */
public class MdbParser {
    /**
     * Syncs image data to a Member object by performing an HTTP request to fetch image data based on
     * the member's first and last name, parsing the response, and adding the image data to the Member object.
     * @param member The Member object to which image data is to be synced.
     * @throws IOException If an I/O error occurs during the HTTP request or while parsing the response.
     */

    public static void syncImgToMember(Member member) throws IOException {
        // Getting data via http request
        String firstName = member.getNachName();
        String lastName = member.getVorName();
        String url = String.format("https://bilddatenbank.bundestag.de/search/picture-result?filterQuery[name][]=%s,+%s&sortVal=3", firstName, lastName);
        HttpURLConnection connection = HttpRequestUtils.getBaseConnection(url);
        StringBuilder response = new StringBuilder();
        HttpRequestUtils.handleHttpRequest(connection, response);
        // Parsing page data to add information about the Member object's avatarring());
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


    /**
     * Parses a Member entity from a given XML Node object. This method extracts member-related information
     * from the XML Node, constructs a Member object with the extracted data, and optionally syncs image data
     * to the Member object by calling syncImgToMember.
     * @param node The XML Node object containing member data.
     * @return A Member object populated with data extracted from the XML Node.
     * @throws IOException If an I/O error occurs during parsing or while syncing image data.
     */
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
