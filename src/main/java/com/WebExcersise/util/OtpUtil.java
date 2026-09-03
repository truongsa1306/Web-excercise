package com.WebExcersise.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public final class OtpUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    public static final int EXPIRATION_MINUTES = 5;

    private OtpUtil() {
    }

    public static String generate() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }

    public static LocalDateTime expiresAt() {
        return LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
    }

    public static boolean isExpired(LocalDateTime expiresAt) {
        return expiresAt == null || LocalDateTime.now().isAfter(expiresAt);
    }
}
