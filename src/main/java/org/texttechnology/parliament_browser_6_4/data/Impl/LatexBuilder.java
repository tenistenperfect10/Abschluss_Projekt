package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.LatexSpeech;
import org.texttechnology.parliament_browser_6_4.helper.StringUtils;

import java.io.*;

public class LatexBuilder {

    private StringBuilder latexCode;

    private static String latexDirectoryPath = "src/main/resources/latex";

    private static String pdfDirectoryPath = "src/main/resources/pdf";

    public static void mkdir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println(path + "目录创建成功");
            } else {
                System.out.println(path + "目录创建失败");
            }
        }
    }

    static {
        mkdir(latexDirectoryPath);
        mkdir(pdfDirectoryPath);
    }

    public LatexBuilder() {
        latexCode = new StringBuilder();

        latexCode.append("% !TeX encoding = UTF-8\n");

        // 添加依赖
        latexCode.append("\\documentclass{article}\n");
        // 处理特殊符号
//        latexCode.append("\\newcommand{\\myescape}[1]{\\string#1}\n");
        latexCode.append("\\usepackage[T1]{fontenc}\n");

        latexCode.append("\\usepackage[utf8]{inputenc}\n");
        latexCode.append("\\DeclareUnicodeCharacter{202F}{\\,}\n");

        // 添加所需的包
        latexCode.append("\\usepackage{graphicx}\n");
        latexCode.append("\\usepackage[ngerman]{babel}\n");
        latexCode.append("\\usepackage{hyperref}\n");
        latexCode.append("\\usepackage{enumitem}\n");
        // 设置hyperref的属性
        latexCode.append("\\hypersetup{colorlinks=true, linkcolor=black}\n");


        latexCode.append("\\setlist[itemize]{topsep=-5pt}\n");

        // 开始文档内容
        latexCode.append("\\begin{document}\n");
    }

    private void makeHeader(LatexSpeech latexSpeech) {
        latexCode.append("\\title{" + latexSpeech.getTitle() + "}\n");
        // 去除自带日期
        latexCode.append("\\date{}\n");
        latexCode.append("\\maketitle\n");

        // 添加目录
        latexCode.append("\\tableofcontents\n");
        latexCode.append("\\newpage\n");
    }

    private void buildFromObject(LatexSpeech latexSpeech) {

        latexSpeech.getSpeechMap().forEach((protocol, speechList) -> {
            latexCode.append("\\section{" + protocol + "}\n");
            speechList.forEach(speech -> {
                latexCode.append("\\subsection{" + speech.getSpeaker() + "}\n");
                latexCode.append("\\noindent\\textbf{Texts:} " + StringUtils.escapeSpecialCharacters(speech.getText()) + "\n");
                latexCode.append("\n"); // 添加一个换行符
                if (speech.getComments().size() > 0) {
                    latexCode.append("\\noindent\\textbf{Comment:}\n");
                    latexCode.append("\\begin{itemize}\n");
                    for (String comment : speech.getComments()) {
                        latexCode.append("    \\setlength\\itemsep{-3pt}\n"); // 减小item之间的间距
                        latexCode.append("    \\item " + comment + "\n");
                    }
                    latexCode.append("\\end{itemize}\n");
                }

            });
        });
        latexCode.append("\\end{document}");
    }

    public String build(LatexSpeech latexSpeech) {
        makeHeader(latexSpeech);
        buildFromObject(latexSpeech);
        generateLatexFile(latexSpeech);
        generatePdf(latexSpeech);
        String filePrefix = generatePdf(latexSpeech);
        // 结束文档内容
        return filePrefix + ".pdf";
    }

    public void generateLatexFile(LatexSpeech latexSpeech) {
        // 确定 LaTeX 文件路径
        String filePrefix = latexSpeech.getTitle().replaceAll(" ", "").replaceAll("/", "-").trim();
        String outputFilePath = latexDirectoryPath + File.separator + filePrefix + ".tex";

        // 将 LaTeX 代码写入文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(latexCode.toString());
            System.out.println("LaTeX 代码成功写入文件：" + outputFilePath);
        } catch (IOException e) {
            System.out.println("写入 LaTeX 代码时出现错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generatePdf(LatexSpeech latexSpeech) {
        String filePrefix = latexSpeech.getTitle().replaceAll(" ", "").replaceAll("/", "-").trim();
        String pdflatexCommand = "pdflatex " + filePrefix + ".tex";


        try {
            // 创建 ProcessBuilder 对象
            ProcessBuilder processBuilder = new ProcessBuilder(pdflatexCommand.split(" "));
            processBuilder.directory(new File("src/main/resources/latex")); // 设置工作目录为 LaTeX 文件所在目录
            processBuilder.redirectErrorStream(true); // 将错误流合并到标准输出流

            // 启动 pdflatex 进程
            Process process = processBuilder.start();

            // 获取进程输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // 输出 pdflatex 进程的标准输出
                System.out.println(line);
            }


            // 等待 pdflatex 进程执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("PDF 文件：" + latexSpeech.getTitle() + "生成成功");
                // 移动 PDF 文件到指定目录
                File sourcePdfFile = new File("src/main/resources/latex/" + filePrefix + ".pdf");
                File targetPdfFile = new File(pdfDirectoryPath, filePrefix + ".pdf");
                // 如果已经存在这个pdf了，先删除，再移动
                if (targetPdfFile.exists()) {
                    boolean deleted = targetPdfFile.delete();
                    if (deleted) {
                        System.out.println("已删除目标 PDF 文件：" + targetPdfFile.getAbsolutePath());
                    } else {
                        System.out.println("无法删除目标 PDF 文件：" + targetPdfFile.getAbsolutePath());
                        return filePrefix;
                    }
                }
                boolean moved = sourcePdfFile.renameTo(targetPdfFile);
                if (moved) {
                    System.out.println("PDF 文件移动成功");
                } else {
                    System.out.println("PDF 文件移动失败");
                }
            } else {
                System.out.println("PDF 文件生成失败。");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return filePrefix;
    }

}
