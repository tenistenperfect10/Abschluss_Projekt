package org.texttechnology.parliament_browser_6_4.helper;

import java.text.SimpleDateFormat;
/**
 * The {@code StringHelper} class provides utility functions for string manipulation,
 * including cleaning and replacing substrings based on predefined rules.
 * It is designed to assist in processing strings that require specific formatting
 * or cleaning operations commonly used throughout the application.
 * @author Giuseppe Abrami
 */
public class StringHelper {
    /**
     * A {@link SimpleDateFormat} constant for formatting dates in a specific pattern.
     */
    public static final SimpleDateFormat DATEOFRMAT = new SimpleDateFormat("dd.MM.YYYY");
    /**
     * An array of prefixes that might be used in names which should be considered for replacement.
     */
    public static String[] FIRSTNAME = {"Dr. h. c.", "Dr."};
    /**
     * An array of regex patterns used for cleaning strings from unwanted characters or patterns.
     */
    public static String[] REGEXCLEAN = {"\\(.*\\)"};
    /**
     * Replaces occurrences of each string in {@code replaceList} within the input string with an empty string,
     * then cleans the resulting string by trimming and removing specific unwanted characters.
     *
     * @param sInput      The input string to be processed.
     * @param replaceList An array of strings that should be replaced with an empty string in the input.
     * @return The cleaned and processed string.
     */
    public static String replaceList(String sInput, String[] replaceList){

        String rString = sInput;

        rString = rString.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}\\p{Z}]", " ");

        for (String s : replaceList) {
            rString = rString.replace(s, "");
        }
        rString = clean(rString);
        return rString;

    }
    /**
     * Cleans the input string by trimming leading and trailing whitespace.
     *
     * @param sInput The input string to be cleaned.
     * @return The cleaned string with removed leading and trailing whitespace.
     */
    public static String clean(String sInput){

        return sInput.trim();

    }

}
