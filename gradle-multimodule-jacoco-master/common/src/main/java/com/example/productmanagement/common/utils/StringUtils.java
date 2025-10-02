package com.example.productmanagement.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for string operations used across the application
 */
public class StringUtils {
    
    private StringUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Truncates a string to a maximum length and adds "..." if it was truncated
     * 
     * @param input the input string
     * @param maxLength the maximum length
     * @return the truncated string
     */
    public static String truncate(String input, int maxLength) {
        if (input == null) {
            return null;
        }
        
        if (input.length() <= maxLength) {
            return input;
        }
        
        return input.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Generates a random alphanumeric string of specified length
     * 
     * @param length the length of the string to generate
     * @return the random string
     */
    public static String randomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
    
    /**
     * Checks if a string is null or empty
     * 
     * @param input the input string
     * @return true if the string is null or empty
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}
