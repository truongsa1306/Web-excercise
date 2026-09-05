package com.WebExcersise.util;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class FormValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern OTP_PATTERN = Pattern.compile("^\\d{6}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+() .-]{0,20}$");

    private FormValidator() {
    }

    public static Map<String, String> errors() {
        return new LinkedHashMap<>();
    }

    public static void required(Map<String, String> errors, String field, String value, String message) {
        if (value == null || value.isBlank()) {
            errors.putIfAbsent(field, message);
        }
    }

    public static void maxLength(Map<String, String> errors, String field, String value, int maxLength, String message) {
        if (value != null && value.trim().length() > maxLength) {
            errors.putIfAbsent(field, message);
        }
    }

    public static void email(Map<String, String> errors, String field, String value) {
        if (value == null || value.isBlank() || !EMAIL_PATTERN.matcher(value.trim()).matches()) {
            errors.putIfAbsent(field, "Email khong hop le");
        }
    }

    public static void otp(Map<String, String> errors, String field, String value) {
        if (value == null || value.isBlank() || !OTP_PATTERN.matcher(value.trim()).matches()) {
            errors.putIfAbsent(field, "OTP phai gom 6 chu so");
        }
    }

    public static void password(Map<String, String> errors, String field, String value) {
        if (value == null || value.length() < 3) {
            errors.putIfAbsent(field, "Mat khau phai co it nhat 3 ky tu");
        }
    }

    public static void phone(Map<String, String> errors, String field, String value) {
        if (value != null && !value.isBlank() && !PHONE_PATTERN.matcher(value.trim()).matches()) {
            errors.putIfAbsent(field, "So dien thoai chi gom so va cac ky tu + ( ) . -");
        }
    }

    public static int integer(Map<String, String> errors, String field, String value, int defaultValue, String message) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            errors.putIfAbsent(field, message);
            return defaultValue;
        }
    }

    public static BigDecimal decimal(Map<String, String> errors, String field, String value, BigDecimal defaultValue, String message) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException exception) {
            errors.putIfAbsent(field, message);
            return defaultValue;
        }
    }

    public static void min(Map<String, String> errors, String field, int value, int min, String message) {
        if (value < min) {
            errors.putIfAbsent(field, message);
        }
    }

    public static void min(Map<String, String> errors, String field, BigDecimal value, BigDecimal min, String message) {
        if (value != null && value.compareTo(min) < 0) {
            errors.putIfAbsent(field, message);
        }
    }

    public static void apply(jakarta.servlet.http.HttpServletRequest request, Map<String, String> errors) {
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("error", errors.values().iterator().next());
        }
    }
}
