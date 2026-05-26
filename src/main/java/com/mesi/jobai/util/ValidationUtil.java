package com.mesi.jobai.util;

/**
 * ValidationUtil provides reusable input validation methods
 * used across Controllers and Services to avoid code duplication.
 */
public class ValidationUtil {

    /**
     * Checks if a string is null or blank (empty/whitespace only).
     * @param value the string to check
     * @return true if null or blank, false otherwise
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Validates a basic email format.
     * Rules: must contain "@" and at least one "." after "@"
     * @param email the email to validate
     * @return true if valid format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (isBlank(email)) return false;
        int atIndex = email.indexOf("@");
        if (atIndex <= 0) return false;
        int dotIndex = email.lastIndexOf(".");
        return dotIndex > atIndex + 1;
    }

    /**
     * Validates a password meets minimum security requirements.
     * Rules: must be at least 6 characters long
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Validates that a user role is one of the allowed values.
     * Allowed: APPLICANT or EMPLOYER (case insensitive)
     * @param role the role string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidRole(String role) {
        return "APPLICANT".equalsIgnoreCase(role) || "EMPLOYER".equalsIgnoreCase(role);
    }

    /**
     * Validates that an ID is a positive integer (greater than zero).
     * @param id the ID to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidId(int id) {
        return id > 0;
    }

    /**
     * Sanitizes user input by trimming whitespace.
     * @param input the raw input string
     * @return trimmed string, or empty string if null
     */
    public static String sanitize(String input) {
        return input == null ? "" : input.trim();
    }
}
