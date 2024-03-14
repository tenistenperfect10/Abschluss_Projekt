package org.texttechnology.parliament_browser_6_4.helper;

import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
/**
 * Provides utility methods for handling files, including zipping and unzipping files,
 * deleting directories, and managing properties files.
 */
public class FileUtils {

    /**
     * Compresses files in the specified directory into a zip file.
     * @param sourceDir Path to the directory containing files to be zipped.
     * @param zipFilePath Path where the zip file will be created.
     * @throws IOException If an I/O error occurs during file processing.
     */
    public static void zipFiles(String sourceDir, String zipFilePath) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        File srcFile = new File(sourceDir);
        addFilesToZip(srcFile, zos, "");
        zos.close();
        fos.close();
    }

    /**
     * Adds files to the zip archive recursively.
     * @param file The file or directory to add to the zip archive.
     * @param zos ZipOutputStream to which the files will be written.
     * @param parentDir The parent directory path for the file or directory being added.
     * @throws IOException If an I/O error occurs.
     */
    private static void addFilesToZip(File file, ZipOutputStream zos, String parentDir) throws IOException {
        if (file.isDirectory()) {
            String zipEntryName = parentDir + file.getName() + "/";
            zos.putNextEntry(new ZipEntry(zipEntryName));
            File[] files = file.listFiles();
            for (File childFile : files) {
                addFilesToZip(childFile, zos, zipEntryName);
            }
        } else {
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(parentDir + file.getName());
            zos.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }
            fis.close();
        }
    }

    /**
     * Recursively deletes a directory and all of its contents.
     * @param directory The directory to be deleted.
     * @throws SecurityException If a security manager exists and denies the delete operation.
     */
    public static void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }

    /**
     * Checks if a file exists at the specified path.
     * @param filePath The path to the file.
     * @return True if the file exists, false otherwise.
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Counts the number of files within a zip archive.
     * @param zipFilePath Path to the zip file.
     * @return The number of files in the zip archive, or null if the file doesn't exist.
     * @throws IOException If an I/O error occurs while reading the zip file.
     */
    public static Integer countFilesInZip(String zipFilePath) {
        if (!fileExists((zipFilePath))) {
            return null;
        }
        int fileCount = 0;
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            fileCount = zipFile.size();
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileCount;
    }

    /**
     * Stores a property in the specified properties file.
     * @param filePath The path to the properties file.
     * @param key The key of the property to store.
     * @param value The value of the property.
     * @param comments Optional comments to include in the file.
     */
    public static void storeProperty(String filePath, String key, String value, String comments) {
        Properties properties = getProperties("properties/keywords.properties");
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            properties.setProperty(key, value);
            properties.store(outputStream, comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads properties from the specified properties file.
     * @param filePath The path to the properties file.
     * @return A Properties object containing the loaded properties.
     * @throws IOException If an error occurs while loading the properties file.
     */
    public static Properties getProperties(String filePath) {
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * Unzips the specified zip file to a destination directory.
     * @param zipFilePath The path to the zip file to be unzipped.
     * @param destDirectory The destination directory where the contents will be extracted.
     * @throws IOException If an I/O error occurs during extraction.
     */
    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        byte[] buffer = new byte[1024];
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                String filePath = destDirectory + File.separator + zipEntry.getName();
                if (!zipEntry.isDirectory()) {
                    extractFile(zipInputStream, filePath, buffer);
                } else {
                    File dir = new File(filePath);
                    dir.mkdirs();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    /**
     * Extracts a file from a ZipInputStream and saves it to the specified path.
     * @param zipInputStream The ZipInputStream from which the file is to be extracted.
     * @param filePath The path where the extracted file will be saved.
     * @param buffer A buffer used for reading data from the ZipInputStream.
     * @throws IOException If an I/O error occurs during file extraction.
     */
    private static void extractFile(ZipInputStream zipInputStream, String filePath, byte[] buffer) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }

}
