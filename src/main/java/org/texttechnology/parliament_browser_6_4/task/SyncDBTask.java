package org.texttechnology.parliament_browser_6_4.task;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.Member;
import org.texttechnology.parliament_browser_6_4.helper.FileUtils;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBUtils;
import org.texttechnology.parliament_browser_6_4.helper.XMLUtils;
import org.texttechnology.parliament_browser_6_4.task.parser.MdbParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;

public class SyncDBTask {

    private static MongoCollection<Document> abgeordnter = MongoDBUtils.getCollection("Abgeordneter");

    private static void unzipFile() {
        String zipFilePath = "src/main/resources/MdB-Stammdaten.zip";
        String destDirectory = "src/main/resources/MdB-Stammdaten/";

        try {
            FileUtils.unzip(zipFilePath, destDirectory);
            System.out.println("解压成功");
        } catch (IOException e) {
            System.out.println("解压时发生错误：" + e.getMessage());
        }
    }

    private static void parseXMLToDB() throws IOException {
        String xmlPath = "src/main/resources/MdB-Stammdaten/MDB_STAMMDATEN.XML";
        org.w3c.dom.Document doc = XMLUtils.getDocument(xmlPath);
        if (doc == null) {
            System.out.println("读取XML文件失败");
            return;
        }
        NodeList nodeList = doc.getElementsByTagName("MDB");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Member member = MdbParser.parseMember(node);
                org.bson.Document document = org.bson.Document.parse(JSON.toJSONString(member));
                document.append("_id", member.getId());
                MongoDBUtils.insertDocument(abgeordnter, document);
            }
            System.out.println("总任务为" + nodeList.getLength() + ", 目前第" + i + "个任务正在执行--------------");
        }
        System.out.println("数据同步任务执行结束");
        FileUtils.deleteDirectory(new File("src/main/resources/MdB-Stammdaten/"));
    }

    public static void main(String[] args) throws IOException {
        unzipFile();
        parseXMLToDB();
    }
}
