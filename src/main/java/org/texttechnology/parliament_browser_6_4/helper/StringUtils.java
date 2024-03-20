package org.texttechnology.parliament_browser_6_4.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handling special characters in latex files that can't be converted properly
 *
 */
public class StringUtils {

    public static String escapeSpecialCharacters(String text) {
        // Regular expressions match special symbols that need to be escaped.
        String regex = "[\\\\{}&_#ʼ$%~^'\"`\\/|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // Storing escaped text with StringBuffer
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            // Escaping special symbols into LaTeX format
            String matched = matcher.group();
            if ("\\\\".equals(matched)) {
                matcher.appendReplacement(stringBuffer, "\\\\\\\\");
            } else {
                matcher.appendReplacement(stringBuffer, "\\\\" + matched);
            }
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }
}
