package org.texttechnology.parliament_browser_6_4.helper;

import java.io.*;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    /**
     * 将指定目录下的文件压缩为一个zip文件
     * @param sourceDir 待压缩的文件目录
     * @param zipFilePath 压缩后的zip文件路径
     * @return 无返回值
     * @throws IOException 如果在文件操作过程中发生异常
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
     * 将文件添加到zip压缩包中
     * @param file 要添加到zip压缩包中的文件或目录
     * @param zos ZipOutputStream对象，用于将文件添加到zip压缩包中
     * @param parentDir 父级目录
     * @throws IOException 如果发生I/O错误
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
     * 递归删除指定目录
     * @param directory 要删除的目录
     * @throws SecurityException 如果没有删除权限
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
     * 检查文件是否存在
     * @param filePath 文件路径
     * @return 如果文件存在则返回true，否则返回false
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 计算压缩文件中的文件数量
     * @param zipFilePath 压缩文件路径
     * @return fileCount 压缩文件中的文件数量
     * @throws IOException 如果发生I/O错误
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
     * 存储属性到指定的文件中
     * @param filePath 文件路径
     * @param key 属性的键
     * @param value 属性的值
     * @param comments 注释
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
     * 获取指定文件路径的属性集合
     * @param filePath 文件路径
     * @return 属性集合
     * @throws IOException 如果发生IO异常
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
     * 解压缩指定的zip文件到目标目录
     * @param zipFilePath 要解压缩的zip文件路径
     * @param destDirectory 目标目录路径
     * @throws IOException 如果发生I/O错误
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
     * 从ZipInputStream中提取文件并保存到指定路径
     * @param zipInputStream ZipInputStream输入流
     * @param filePath 文件保存路径
     * @param buffer 用于读取的字节数组
     * @throws IOException 如果发生I/O错误
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
