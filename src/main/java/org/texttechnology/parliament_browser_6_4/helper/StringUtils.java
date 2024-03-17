package org.texttechnology.parliament_browser_6_4.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String escapeSpecialCharacters(String text) {
        // 正则表达式匹配需要转义的特殊符号
        String regex = "[\\\\{}&_#ʼ$%~^'\"`\\/|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // 使用 StringBuffer 存储转义后的文本
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            // 将特殊符号转义为 LaTeX 格式
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
