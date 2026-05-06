package com.ngoconnect.util;

import com.ngoconnect.exception.NGOConnectException;

public class Validator {

    public static void requireNonEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new NGOConnectException(fieldName + " cannot be empty.");
        }
    }

    public static void requirePositiveYear(int year, String fieldName) {
        int currentYear = java.time.Year.now().getValue();
        if (year < 1900 || year > currentYear) {
            throw new NGOConnectException(fieldName + " must be between 1900 and " + currentYear + ".");
        }
    }

    public static void requireValidEmail(String email) {
        if (email == null || !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            throw new NGOConnectException("Invalid email format: " + email);
        }
    }

    public static void requireValidPhone(String phone) {
        if (phone == null || !phone.replaceAll("[\\s\\-+()]", "").matches("\\d{7,15}")) {
            throw new NGOConnectException("Invalid phone number: " + phone);
        }
    }

    public static int parseIntSafe(String input, String fieldName) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            throw new NGOConnectException(fieldName + " must be a valid integer.");
        }
    }

    public static double parseDoubleSafe(String input, String fieldName) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            throw new NGOConnectException(fieldName + " must be a valid number.");
        }
    }
}
