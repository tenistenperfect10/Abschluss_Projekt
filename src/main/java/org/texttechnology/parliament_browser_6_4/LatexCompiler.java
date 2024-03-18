package org.texttechnology.parliament_browser_6_4;

import java.io.*;

public class LatexCompiler {

    public static void main(String[] args) {
        String textSource = "\\documentclass{article}\n" +
                "\\begin{document}\n" +
                "Hello, LaTeX!\n" +
                "\\end{document}";


        ProcessBuilder processBuilder = new ProcessBuilder("pdflatex", "-synctex=1", "-interaction=nonstopmode");
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            BufferedWriter outputFileWriter = getBufferedWriter(process, textSource);
            outputFileWriter.close();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("The PDF file was generated successfully.");
            } else {
                System.out.println("PDF file generation failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedWriter getBufferedWriter(Process process, String textSource) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        writer.write(textSource);
        writer.flush();
        writer.close();

        // Redirects the output of pdflatex to the standard output at the same time
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedWriter outputConsoleWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        String line;
        while ((line = reader.readLine()) != null) {
            outputConsoleWriter.write(line + "\n");
        }
        return outputConsoleWriter;
    }
}
