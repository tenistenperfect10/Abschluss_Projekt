package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.LatexSpeech;
import org.texttechnology.parliament_browser_6_4.helper.StringUtils;

import java.io.*;

/**
 * Setting the contents of the latex and the output path of the file
 */
public class LatexBuilder {

    private StringBuilder latexCode;

    private static String latexDirectoryPath = "src/main/resources/latex";

    private static String pdfDirectoryPath = "src/main/resources/pdf";

    public static void mkdir(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println(path + "Directory created successfully");
            } else {
                System.out.println(path + "Directory creation failure");
            }
        }
    }

    static {
        mkdir(latexDirectoryPath);
        mkdir(pdfDirectoryPath);
    }

    /**
     * set a latex
     */
    public LatexBuilder() {
        latexCode = new StringBuilder();

        latexCode.append("% !TeX encoding = UTF-8\n");

        // Adding Dependencies
        latexCode.append("\\documentclass{article}\n");
        // Handling of special symbols
//        latexCode.append("\\newcommand{\\myescape}[1]{\\string#1}\n");
        latexCode.append("\\usepackage[T1]{fontenc}\n");

        latexCode.append("\\usepackage[utf8]{inputenc}\n");
        latexCode.append("\\DeclareUnicodeCharacter{202F}{\\,}\n");

        // Add the required packages
        latexCode.append("\\usepackage{graphicx}\n");
        latexCode.append("\\usepackage[ngerman]{babel}\n");
        latexCode.append("\\usepackage{hyperref}\n");
        latexCode.append("\\usepackage{enumitem}\n");
        // Setting properties of hyperref
        latexCode.append("\\hypersetup{colorlinks=true, linkcolor=black}\n");


        latexCode.append("\\setlist[itemize]{topsep=-5pt}\n");

        // Start document content
        latexCode.append("\\begin{document}\n");
    }

    /**
     * set the header of latex data
     * @param latexSpeech
     */
    private void makeHeader(LatexSpeech latexSpeech) {
        latexCode.append("\\title{" + latexSpeech.getTitle() + "}\n");
        // Remove Self-Date
        latexCode.append("\\date{}\n");
        latexCode.append("\\maketitle\n");

        // Add an index
        latexCode.append("\\tableofcontents\n");
        latexCode.append("\\newpage\n");
    }

    /**
     * set the section of latex
     * @param latexSpeech
     */

    private void buildFromObject(LatexSpeech latexSpeech) {

        latexSpeech.getSpeechMap().forEach((protocol, speechList) -> {
            latexCode.append("\\section{" + protocol + "}\n");
            speechList.forEach(speech -> {
                latexCode.append("\\subsection{" + speech.getSpeaker() + "}\n");
                latexCode.append("\\noindent\\textbf{Texts:} " + StringUtils.escapeSpecialCharacters(speech.getText()) + "\n");
                latexCode.append("\n"); // Add a line break
                if (speech.getComments().size() > 0) {
                    latexCode.append("\\noindent\\textbf{Comment:}\n");
                    latexCode.append("\\begin{itemize}\n");
                    for (String comment : speech.getComments()) {
                        latexCode.append("    \\setlength\\itemsep{-3pt}\n"); // Reduce the spacing between items
                        latexCode.append("    \\item " + comment + "\n");
                    }
                    latexCode.append("\\end{itemize}\n");
                }

            });
        });
        latexCode.append("\\end{document}");
    }

    /**
     * content document
     * @param latexSpeech
     * @return
     */
    public String build(LatexSpeech latexSpeech) {
        makeHeader(latexSpeech);
        buildFromObject(latexSpeech);
        generateLatexFile(latexSpeech);
        generatePdf(latexSpeech);
        String filePrefix = generatePdf(latexSpeech);
        // End document content
        return filePrefix + ".pdf";
    }

    /**
     * process latex
     * @param latexSpeech
     */

    public void generateLatexFile(LatexSpeech latexSpeech) {
        // Determine the path to the LaTeX file
        String filePrefix = latexSpeech.getTitle().replaceAll(" ", "").replaceAll("/", "-").trim();
        String outputFilePath = latexDirectoryPath + File.separator + filePrefix + ".tex";

        // Writing LaTeX code to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(latexCode.toString());
            System.out.println("LaTeX code successfully written to file：" + outputFilePath);
        } catch (IOException e) {
            System.out.println("An error occurs when writing LaTeX code:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * set the pdf of latex
     * @param latexSpeech
     * @return
     */

    private String generatePdf(LatexSpeech latexSpeech) {
        String filePrefix = latexSpeech.getTitle().replaceAll(" ", "").replaceAll("/", "-").trim();
        String pdflatexCommand = "pdflatex " + filePrefix + ".tex";


        try {
            // Creating ProcessBuilder Objects
            ProcessBuilder processBuilder = new ProcessBuilder(pdflatexCommand.split(" "));
            processBuilder.directory(new File("src/main/resources/latex")); // Set the working directory to the directory where the LaTeX files are located
            processBuilder.redirectErrorStream(true); // Merge the error stream into the standard output stream

            // Start the pdflatex process
            Process process = processBuilder.start();

            // Get the process output stream
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Output standard output of the pdflatex process
                System.out.println(line);
            }


            // Waiting for the pdflatex process to finish
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("PDF ：" + latexSpeech.getTitle() + "Generated successfully");
                // Move PDF files to a specified directory
                File sourcePdfFile = new File("src/main/resources/latex/" + filePrefix + ".pdf");
                File targetPdfFile = new File(pdfDirectoryPath, filePrefix + ".pdf");
                // If the pdf already exists, delete it first, then move it.
                if (targetPdfFile.exists()) {
                    boolean deleted = targetPdfFile.delete();
                    if (deleted) {
                        System.out.println("Deleted target PDF file：" + targetPdfFile.getAbsolutePath());
                    } else {
                        System.out.println("Unable to delete target PDF file：" + targetPdfFile.getAbsolutePath());
                        return filePrefix;
                    }
                }
                boolean moved = sourcePdfFile.renameTo(targetPdfFile);
                if (moved) {
                    System.out.println("PDF file moved successfully");
                } else {
                    System.out.println("PDF file move failure");
                }
            } else {
                System.out.println("PDF file generation failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return filePrefix;
    }

}
