package com.mesi.jobai.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DateUtil provides helper methods for formatting and
 * parsing date/time values displayed across the application UI.
 */
public class DateUtil {

    // Standard display format used in the UI tables and labels
    private static final DateTimeFormatter DISPLAY_FORMAT =
        DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    // Short date format used in compact views
    private static final DateTimeFormatter SHORT_FORMAT =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Formats a raw database timestamp string into a friendly readable format.
     * Example: "2025-05-20T14:30:00" → "20 May 2025, 14:30"
     * @param rawTimestamp the raw ISO timestamp from the database
     * @return formatted string, or the original if parsing fails
     */
    public static String formatDisplay(String rawTimestamp) {
        if (rawTimestamp == null || rawTimestamp.isEmpty()) return "N/A";
        try {
            // Handle both full ISO format and postgres timestamp format
            String cleaned = rawTimestamp.replace(" ", "T");
            if (cleaned.length() > 19) cleaned = cleaned.substring(0, 19);
            LocalDateTime dt = LocalDateTime.parse(cleaned);
            return dt.format(DISPLAY_FORMAT);
        } catch (Exception e) {
            // Return raw value if it cannot be parsed
            return rawTimestamp;
        }
    }

    /**
     * Formats a raw database timestamp string into a short date.
     * Example: "2025-05-20T14:30:00" → "20/05/2025"
     * @param rawTimestamp the raw ISO timestamp from the database
     * @return short date string, or "N/A" if null
     */
    public static String formatShort(String rawTimestamp) {
        if (rawTimestamp == null || rawTimestamp.isEmpty()) return "N/A";
        try {
            String cleaned = rawTimestamp.replace(" ", "T");
            if (cleaned.length() > 19) cleaned = cleaned.substring(0, 19);
            LocalDateTime dt = LocalDateTime.parse(cleaned);
            return dt.format(SHORT_FORMAT);
        } catch (Exception e) {
            return rawTimestamp;
        }
    }

    /**
     * Returns the current timestamp as a formatted display string.
     * @return current datetime in display format
     */
    public static String now() {
        return LocalDateTime.now().format(DISPLAY_FORMAT);
    }
}
