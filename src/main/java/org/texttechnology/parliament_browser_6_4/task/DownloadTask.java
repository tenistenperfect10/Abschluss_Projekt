
package org.texttechnology.parliament_browser_6_4.task;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.texttechnology.parliament_browser_6_4.helper.FileUtils;
import org.texttechnology.parliament_browser_6_4.helper.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.texttechnology.parliament_browser_6_4.helper.FileUtils.deleteDirectory;
import static org.texttechnology.parliament_browser_6_4.helper.FileUtils.storeProperty;
/**
 * Handles the tasks of downloading, unzipping, and organizing data related to parliamentary speeches and documents.
 * This includes managing directories, downloading DTDs and ZIP files, parsing URLs, and synchronizing XML data.
 * @author He Liu
 * @author Yingzhu Chen
 */
public class DownloadTask {

    // path for resources
    public static String dir = "src/main/resources";

    // Directory for storing ZIP files
    public static String zipDir = dir + File.separator + "Bundestagreden" + File.separator;

    // Directory object for the ZIP storage
    public static File directory = new File(zipDir);

    // Thread-safe map for storing download information
    private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    // Set for storing metadata information for download
    private static final Set<String> relativeSet = new HashSet<>();

    //  List of DTD URLs to download
    private static List<String> dtdsUrls = new ArrayList<>();

    // thread pool
    private static ExecutorService executorService ;

    // Static Code Block Initialization
    static {
        Properties properties = FileUtils.getProperties("properties/keywords.properties");
        relativeSet.addAll(Arrays.asList(properties.getProperty("keywords").split(",")));
        dtdsUrls = Arrays.asList(properties.getProperty("dtds").split(","));
        executorService = Executors.newFixedThreadPool(relativeSet.size());
    }


    /**
     * Checks if a specified ZIP file has been completely downloaded and is intact.
     * @param name The name of the ZIP file to check.
     * @param expect The expected number of files within the ZIP.
     * @return True if the file is fully downloaded and intact, otherwise false.
     */
    private static boolean check(String name, Integer expect) {
        String filePath = dir + File.separator + name + ".zip";
        Integer count = FileUtils.countFilesInZip(filePath);
        if (count == null) {
            System.out.println(name + ".zip file not downloaded");
            return false;
        }
        if (count < expect) {
            System.out.println(name + ".zip file not fully downloaded");
            return false;
        }
        System.out.println(name + ".zip file has been fully downloaded");
        return true;
    }

    /**
     * Creates a directory for storing downloaded files.
     * @param name The name of the directory to create.
     */
    public static void makeDir(String name) {
        if (directory.exists()) {
            deleteDirectory(directory); // Delete the directory and all its subfiles
            deleteDirectory(new File(dir + File.separator + name + ".zip"));
        }
        directory.mkdirs(); // Creating directories and their parents
    }

    /**
     * Downloads DTD files from predefined URLs.
     * @throws IOException If an I/O exception occurs.
     */
    private static void downloadDtds() throws IOException {
        for (int i = 0; i < dtdsUrls.size(); i++) {
            HttpRequestUtils.downloadFile(dtdsUrls.get(i), zipDir + (i + 1) + ".dtd");
        }
        System.out.println("The dtd file download is complete!!!");
    }

    /**
     * Downloads a ZIP file
     * @throws IOException If an I/O exception occurs.
     */
    private static void downloadZip() throws IOException {
        Properties properties = FileUtils.getProperties("properties/keywords.properties");
        deleteDirectory(new File("src/main/resources/MdB-Stammdaten.zip"));
        HttpRequestUtils.downloadFile(properties.getProperty("zipUrl"), "src/main/resources/MdB-Stammdaten.zip");
    }

    /**
     * Initializes the request URLs for downloading based on specific criteria.
     * @return A list of relative IDs useful for constructing request URLs.
     * @throws IOException If an I/O exception occurs.
     */
    private static List<String> initRequestUrl() throws IOException {
        HttpURLConnection connection = HttpRequestUtils.getBaseConnection("https://www.bundestag.de/services/opendata");
        StringBuilder response = new StringBuilder();
        HttpRequestUtils.handleHttpRequest(connection, response);
        Document doc = Jsoup.parse(response.toString());

        Elements sections = doc.select("section"); // Selecting elements by class name
        List<String> relativeIdList = new ArrayList<>();
        for (Element section : sections) {
            Element title = section.selectFirst(".bt-title");
            if (relativeSet.contains(title.text())) {
                String id = section.attr("id").replace("mod", "");
                relativeIdList.add(id);
            }
        }
        return relativeIdList;
    }

    /**
     * Constructs a URL based on an identifier and offset for pagination.
     * @param id The identifier for constructing the URL.
     * @param offset The offset for pagination.
     * @return The constructed URL as a String.
     */
    private static String buildUrl(String id, int offset) {
        String url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/" + id + "-" + id + "?limit=10&noFilterSet=true&offset=" + offset;
        System.out.println(url);
        return url;
    }

    /**
     * Builds a map of download links by parsing the specified id and offsets,
     * facilitating the download of files and their packaging into ZIP files.
     * @param id The identifier to use for constructing download URLs.
     * @throws IOException If an I/O exception occurs.
     */
    private static void buildDownloadMap(String id) throws IOException {
        int offsetIdx = 0;
        while (true) {
            HttpURLConnection connection = HttpRequestUtils.getBaseConnection(buildUrl(id, offsetIdx * 10));
            offsetIdx += 1;
            StringBuilder response = new StringBuilder();
            HttpRequestUtils.handleHttpRequest(connection, response);
            Document doc = Jsoup.parse(response.toString());
            Elements elements = doc.select(".bt-documents-description");
            // Exit, if the corresponding DOM is not found
            if (elements.isEmpty()) {
                return;
            }
            for (Element element : elements) {
                String title = element.selectFirst("strong").text();
                String link = "https://www.bundestag.de" + element.selectFirst(".bt-link-dokument").attr("href");
                map.put(title, link);
            }
        }
    }

    /**
     * Downloads XML files based on links stored in a map and packages them into a ZIP file.
     * @throws IOException If an I/O exception occurs.
     */
    private static void downloadXml() throws IOException {
        final int[] count = {0};
        // Iterate through the download file
        map.forEach((key, value) -> {
            try {
                System.out.println(key  + " ->" + value);
                count[0]++;
                HttpRequestUtils.downloadFile(value,   zipDir + count[0] + ".xml");
            } catch (IOException e) {
                System.out.println(key + "IO exception for the corresponding file:" + e);
            }
        });
        // Compress files and delete useless folders
        FileUtils.zipFiles(zipDir, dir + File.separator + "Bundestagreden.zip");
        deleteDirectory(directory); // Delete the directory and all its subfiles
        System.out.println("xml file download complete!!!");
    }

    private static void storeFileCount() {
        int count = map.size() + 2;
        storeProperty("src/main/resources/properties/keywords.properties", "Bundestagreden", String.valueOf(count), null);
    }

    /**
     * The main method to execute the download task. It orchestrates the entire process,
     * including checking existing files, making directories, initializing URLs, downloading,
     * and storing data.
     * @param args Command-line arguments.
     * @throws Exception If any exception occurs during the process.
     */
    public static void main(String[] args) throws Exception {
        if (!check("MdB-Stammdaten", 2)) {
            downloadZip();
        }
        Properties properties = FileUtils.getProperties("properties/keywords.properties");
        if (check("Bundestagreden", Integer.valueOf(properties.getProperty("Bundestagreden")))) {
            return;
        }
        makeDir("Bundestagreden");
        List<String> ids = initRequestUrl();
        for (String id : ids) {
            executorService.submit(() -> {
                try {
                    buildDownloadMap(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.submit(() -> {
            try {
                downloadDtds();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //  Wait for the asynchronous thread to finish sorting out the download information and then unify the downloads
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        downloadXml();
        System.out.println("The number of elements in the Map is: " + map.size());
        storeFileCount();
    }
}
