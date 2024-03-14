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

/**
 * A task class for synchronizing the data from an XML file to a MongoDB database.
 * It involves unzipping the XML data file and parsing each member's data to be stored in the database.
 */

public class SyncDBTask {
    // Collection in MongoDB to store Member documents

    private static MongoCollection<Document> abgeordnter = MongoDBUtils.getCollection("Abgeordnter");

    /**
     * Unzips the specified XML data file for processing.
     * This method extracts the contents of the ZIP file into a designated directory for further processing.
     */
    private static void unzipFile() {
        String zipFilePath = "src/main/resources/MdB-Stammdaten.zip";
        String destDirectory = "src/main/resources/MdB-Stammdaten/";

        try {
            FileUtils.unzip(zipFilePath, destDirectory);
            System.out.println("Unzip successfully");
        } catch (IOException e) {
            System.out.println("An error occurred while decompressing:" + e.getMessage());
        }
    }

    private static void parseXMLToDB() throws IOException {
        String xmlPath = "src/main/resources/MdB-Stammdaten/MDB_STAMMDATEN.XML";
        org.w3c.dom.Document doc = XMLUtils.getDocument(xmlPath);
        if (doc == null) {
            System.out.println("Failed to read XML file");
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
            System.out.println("The total tasks are" + nodeList.getLength() + ", Current" + i + "task is currently being executed--------------");
        }
        System.out.println("End of data synchronization task execution");
        FileUtils.deleteDirectory(new File("src/main/resources/MdB-Stammdaten/"));
    }
    /**
     * The main method to run the synchronization task.
     * It performs the steps required to unzip the XML data file and parse the contents into the MongoDB database.
     * @param args Command-line arguments (not used).
     * @throws IOException If there is an issue with file access, reading, or writing.
     */
    public static void main(String[] args) throws IOException {
        unzipFile();
        parseXMLToDB();
    }
}
