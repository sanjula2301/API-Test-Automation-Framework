package com.fintech.api.config;

public final class FrameworkConfig {

    private FrameworkConfig() {
    }

    public static String cryptoBaseUrl() {
        return systemProperty("crypto.base.url", "https://api.coingecko.com/api/v3");
    }

    public static String exchangeBaseUrl() {
        return systemProperty("exchange.base.url", "https://open.er-api.com/v6");
    }

    public static String bookingBaseUrl() {
        return systemProperty("booking.base.url", "https://restful-booker.herokuapp.com");
    }

    public static long testDelayMillis() {
        return longProperty("api.test.delay.ms", 2000L);
    }

    public static int apiTimeoutMillis() {
        return (int) longProperty("api.timeout.ms", 10000L);
    }

    public static boolean relaxedHttpsValidationEnabled() {
        return booleanProperty("api.relaxed.https", true);
    }

    private static String systemProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private static long longProperty(String key, long defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Long.parseLong(value);
    }

    private static boolean booleanProperty(String key, boolean defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
