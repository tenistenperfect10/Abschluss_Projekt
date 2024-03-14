package org.texttechnology.parliament_browser_6_4.helper;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
/**
 * A utility class providing methods for working with XML files,
 * specifically for converting XML files into Document objects.
 */
public class XMLUtils {
    /**
     * Parses the XML file specified by the filePath into a Document object.
     *
     * This method uses the DocumentBuilderFactory and DocumentBuilder classes
     * from the javax.xml.parsers package to parse the XML file. It handles
     * the necessary exceptions and returns null if parsing fails for any reason,
     * including file not found, parsing errors, or configuration issues with the parser.
     *
     * @param filePath The path to the XML file to be parsed.
     * @return A Document object representing the parsed XML file, or null if parsing fails.
     */

    public static Document getDocument(String filePath) {
        try {
            File file = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

