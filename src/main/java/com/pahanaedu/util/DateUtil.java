package com.pahanaedu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for date formatting and manipulation
 */
public class DateUtil {
    
    // Date format patterns
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DISPLAY_DATETIME_FORMAT = "dd/MM/yyyy hh:mm a";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm a";
    public static final String BILL_DATE_FORMAT = "dd-MMM-yyyy";
    
    // SimpleDateFormat instances
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATETIME_FORMAT);
    private static final SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
    private static final SimpleDateFormat displayDateTimeFormat = new SimpleDateFormat(DISPLAY_DATETIME_FORMAT);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
    private static final SimpleDateFormat displayTimeFormat = new SimpleDateFormat(DISPLAY_TIME_FORMAT);
    private static final SimpleDateFormat billDateFormat = new SimpleDateFormat(BILL_DATE_FORMAT);
    
    /**
     * Get current date as string in default format (yyyy-MM-dd)
     */
    public static String getCurrentDate() {
        return dateFormat.format(new Date());
    }
    
    /**
     * Get current datetime as string in default format (yyyy-MM-dd HH:mm:ss)
     */
    public static String getCurrentDateTime() {
        return dateTimeFormat.format(new Date());
    }
    
    /**
     * Get current time as string in default format (HH:mm:ss)
     */
    public static String getCurrentTime() {
        return timeFormat.format(new Date());
    }
    
    /**
     * Format date to string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return dateFormat.format(date);
    }
    
    /**
     * Format datetime to string
     */
    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }
        return dateTimeFormat.format(date);
    }
    
    /**
     * Format date for display (dd/MM/yyyy)
     */
    public static String formatDateForDisplay(Date date) {
        if (date == null) {
            return "";
        }
        return displayDateFormat.format(date);
    }
    
    /**
     * Format datetime for display (dd/MM/yyyy hh:mm a)
     */
    public static String formatDateTimeForDisplay(Date date) {
        if (date == null) {
            return "";
        }
        return displayDateTimeFormat.format(date);
    }
    
    /**
     * Format time for display (hh:mm a)
     */
    public static String formatTimeForDisplay(Date date) {
        if (date == null) {
            return "";
        }
        return displayTimeFormat.format(date);
    }
    
    /**
     * Format date for bills (dd-MMM-yyyy)
     */
    public static String formatDateForBill(Date date) {
        if (date == null) {
            return "";
        }
        return billDateFormat.format(date);
    }
    
    /**
     * Parse string to date
     */
    public static Date parseDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return dateFormat.parse(dateString);
    }
    
    /**
     * Parse string to datetime
     */
    public static Date parseDateTime(String dateTimeString) throws ParseException {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return dateTimeFormat.parse(dateTimeString);
    }
    
    /**
     * Parse display format string to date
     */
    public static Date parseDisplayDate(String dateString) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return displayDateFormat.parse(dateString);
    }
    
    /**
     * Convert SQL Date to Util Date
     */
    public static Date sqlToUtil(java.sql.Date sqlDate) {
        if (sqlDate == null) {
            return null;
        }
        return new Date(sqlDate.getTime());
    }
    
    /**
     * Convert Util Date to SQL Date
     */
    public static java.sql.Date utilToSql(Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new java.sql.Date(utilDate.getTime());
    }
    
    /**
     * Convert Util Date to SQL Timestamp
     */
    public static java.sql.Timestamp utilToTimestamp(Date utilDate) {
        if (utilDate == null) {
            return null;
        }
        return new java.sql.Timestamp(utilDate.getTime());
    }
    
    /**
     * Get current SQL Date
     */
    public static java.sql.Date getCurrentSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }
    
    /**
     * Get current SQL Timestamp
     */
    public static java.sql.Timestamp getCurrentTimestamp() {
        return new java.sql.Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Calculate age from date of birth
     */
    public static int calculateAge(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        
        LocalDate birthDate = new java.sql.Date(dateOfBirth.getTime()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        
        return currentDate.getYear() - birthDate.getYear();
    }
    
    /**
     * Check if date is in the past
     */
    public static boolean isPastDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(new Date());
    }
    
    /**
     * Check if date is in the future
     */
    public static boolean isFutureDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.after(new Date());
    }
    
    /**
     * Get start of day
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        try {
            String dateStr = formatDate(date) + " 00:00:00";
            return parseDateTime(dateStr);
        } catch (ParseException e) {
            return date;
        }
    }
    
    /**
     * Get end of day
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        try {
            String dateStr = formatDate(date) + " 23:59:59";
            return parseDateTime(dateStr);
        } catch (ParseException e) {
            return date;
        }
    }
    
    /**
     * Format date with custom pattern
     * Add this method to your existing DateUtil class
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null || pattern == null) {
            return "";
        }
        try {
            SimpleDateFormat customFormat = new SimpleDateFormat(pattern);
            return customFormat.format(date);
        } catch (Exception e) {
            // If pattern is invalid, use default format
            return formatDate(date);
        }
    }
}