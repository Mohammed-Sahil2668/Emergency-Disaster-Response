package com.emergency.response.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        String cleanPhone = phone.replaceAll("[^0-9]", "");
        return cleanPhone.length() >= 10;
    }

    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
