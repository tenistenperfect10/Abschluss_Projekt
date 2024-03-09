package org.texttechnology.parliament_browser_6_4.helper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtils {

    /**
     * 获取连接并进行基本设置
     * @param urlStr
     * @return
     * @throws IOException
     */
    public static HttpURLConnection getBaseConnection(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestProperty("Accept", "application/json;charset=UTF-8");
        return connection;
    }

    /**
     * 处理请求和响应
     * @param connection
     * @param response
     * @throws IOException
     */
    public static void handleHttpRequest(HttpURLConnection connection, StringBuilder response) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
        } else {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                response.append(errorLine);
            }
            errorReader.close();
        }
    }

    /**
     * 下载文件
     * @param fileUrl 文件的下载链接
     * @param savePath 文件保存路径
     * @throws IOException
     */
    public static void downloadFile(String fileUrl, String savePath) throws IOException {
        HttpURLConnection connection = getBaseConnection(fileUrl);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(savePath);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
        } else {
            // 处理错误
            System.out.println("Failed to download file. HTTP error code: " + responseCode);
        }
    }
}
