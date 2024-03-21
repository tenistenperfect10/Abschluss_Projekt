package org.texttechnology.parliament_browser_6_4.helper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * A utility class for handling HTTP requests, including setting up connections, processing requests and responses,
 * and downloading files from specified URLs.
 * @author Yingzhu Chen
 * @author He Liu
 */
public class HttpRequestUtils {

    /**
     * Opens a connection to the specified URL and sets up basic properties such as timeouts and request headers.
     * @param urlStr The URL as a String to which the connection is to be opened.
     * @return An HttpURLConnection object configured with connection timeouts and request properties.
     * @throws IOException If an I/O exception occurs while opening the connection.
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
     * Processes an HTTP request by reading the response from the given connection and appending it to a StringBuilder.
     * Handles both successful and error responses.
     * @param connection The HttpURLConnection from which the response is to be read.
     * @param response A StringBuilder to which the response is appended.
     * @throws IOException If an I/O exception occurs while reading the response.
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
     * Downloads a file from the specified URL and saves it to the provided path.
     * The method establishes a connection to the URL, reads the content, and writes it to a file.
     * @param fileUrl The URL of the file to download.
     * @param savePath The file system path where the downloaded file should be saved.
     * @throws IOException If an I/O exception occurs during the download process.
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
            // process error
            System.out.println("Failed to download file. HTTP error code: " + responseCode);
        }
    }
}
