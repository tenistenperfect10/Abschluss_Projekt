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

public class DownloadTask {

    // 根目录
    public static String dir = "src/main/resources";

    // 压缩包文件名
    public static String zipDir = dir + File.separator + "Bundestagreden" + File.separator;

    // 目录
    public static File directory = new File(zipDir);

    // 存储需要下载的文件信息（线程安全）
    private static final ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

    // 待下载的元数据信息
    private static final Set<String> relativeSet = new HashSet<>();

    // 待下载的dtd链接
    private static List<String> dtdsUrls = new ArrayList<>();

    // 线程池
    private static ExecutorService executorService ;

    // 静态代码块初始化
    static {
        Properties properties = FileUtils.getProperties("properties/keywords.properties");
        relativeSet.addAll(Arrays.asList(properties.getProperty("keywords").split(",")));
        dtdsUrls = Arrays.asList(properties.getProperty("dtds").split(","));
        executorService = Executors.newFixedThreadPool(relativeSet.size());
    }


    /**
     * 检查指定名称的压缩文件是否下载完成并且完整
     * @param name 指定的文件名称
     * @param expect 期望的文件数量
     * @return 如果文件已全部下载完成且完整则返回true，否则返回false
     */
    private static boolean check(String name, Integer expect) {
        String filePath = dir + File.separator + name + ".zip";
        Integer count = FileUtils.countFilesInZip(filePath);
        if (count == null) {
            System.out.println(name + ".zip未下载");
            return false;
        }
        if (count < expect) {
            System.out.println(name + ".zip文件未完整下载");
            return false;
        }
        System.out.println(name + ".zip文件已全部下载完成");
        return true;
    }

    /**
     * 创建目录
     * @param name 目录名称
     */
    public static void makeDir(String name) {
        if (directory.exists()) {
            deleteDirectory(directory); // 删除目录及其所有子文件
            deleteDirectory(new File(dir + File.separator + name + ".zip"));
        }
        directory.mkdirs(); // 创建目录及其父目录
    }

    /**
     * 下载DTD文件
     * @throws IOException 输入输出异常
     */
    private static void downloadDtds() throws IOException {
        for (int i = 0; i < dtdsUrls.size(); i++) {
            HttpRequestUtils.downloadFile(dtdsUrls.get(i), zipDir + (i + 1) + ".dtd");
        }
        System.out.println("dtd文件下载完成！！！");
    }

    /**
     * 下载ZIP文件
     * @throws IOException 输入输出异常
     */
    private static void downloadZip() throws IOException {
        Properties properties = FileUtils.getProperties("properties/keywords.properties");
        deleteDirectory(new File("src/main/resources/MdB-Stammdaten.zip"));
        HttpRequestUtils.downloadFile(properties.getProperty("zipUrl"), "src/main/resources/MdB-Stammdaten.zip");
    }

    /**
     * 初始化请求URL
     * @return 相对ID列表
     * @throws IOException 输入输出异常
     */
    private static List<String> initRequestUrl() throws IOException {
        HttpURLConnection connection = HttpRequestUtils.getBaseConnection("https://www.bundestag.de/services/opendata");
        StringBuilder response = new StringBuilder();
        HttpRequestUtils.handleHttpRequest(connection, response);
        Document doc = Jsoup.parse(response.toString());

        Elements sections = doc.select("section"); // 通过类名选择元素
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
     * 构建URL链接
     * @param id 标识符
     * @return url 构建的URL链接
     */
    private static String buildUrl(String id, int offset) {
        String url = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/" + id + "-" + id + "?limit=10&noFilterSet=true&offset=" + offset;
        System.out.println(url);
        return url;
    }

    /**
     * 下载指定id对应的文件，并打包成zip文件
     * @param id 指定的id
     * @throws IOException 输入输出异常
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
            // 如果找不到对应的DOM，则退出
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
     * 下载文件并压缩
     * @throws IOException 如果发生I/O异常
     */
    private static void downloadXml() throws IOException {
        final int[] count = {0};
        // 遍历下载文件
        map.forEach((key, value) -> {
            try {
                System.out.println(key  + " ->" + value);
                count[0]++;
                HttpRequestUtils.downloadFile(value,   zipDir + count[0] + ".xml");
            } catch (IOException e) {
                System.out.println(key + "对应的文件出现IO异常:" + e);
            }
        });
        // 压缩文件并删除无用文件夹
        FileUtils.zipFiles(zipDir, dir + File.separator + "Bundestagreden.zip");
        deleteDirectory(directory); // 删除目录及其所有子文件
        System.out.println("xml文件下载完成！！！");
    }

    private static void storeFileCount() {
        int count = map.size() + 2;
        storeProperty("src/main/resources/properties/keywords.properties", "Bundestagreden", String.valueOf(count), null);
    }

    /**
     * 主方法，初始化请求URL并下载数据
     * @param args 命令行参数
     * @throws IOException 输入输出异常
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
        // 等待异步线程整理完下载信息后统一下载
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        downloadXml();
        System.out.println("Map中的元素个数为: " + map.size());
        storeFileCount();
    }
}
