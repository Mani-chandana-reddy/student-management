package com.chandana.sms.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Small input checks used by the service layer.
 * They do not touch the database, so they are easy to unit test.
 */
public final class ValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private ValidationUtil() {}

    public static boolean isBlank(String value) {
        return StringUtils.isBlank(value);
    }

    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean isValidAge(int age) {
        return age > 0 && age <= 100;
    }

    public static boolean isValidYear(int year) {
        return year >= 1 && year <= 6;
    }

    public static boolean isValidMarks(double marks) {
        return marks >= 0 && marks <= 100;
    }
}
