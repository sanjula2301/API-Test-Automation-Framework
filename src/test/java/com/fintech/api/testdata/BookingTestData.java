package com.fintech.api.testdata;

import java.util.HashMap;
import java.util.Map;

public final class BookingTestData {

    private BookingTestData() {
    }

    public static Map<String, String> validCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "admin");
        credentials.put("password", "password123");
        return credentials;
    }

    public static Map<String, String> invalidCredentials() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "wronguser");
        credentials.put("password", "wrongpass");
        return credentials;
    }

    public static Map<String, Object> booking(
        String firstName,
        String lastName,
        int totalPrice,
        boolean depositPaid,
        String checkIn,
        String checkOut,
        String additionalNeeds
    ) {
        Map<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", checkIn);
        bookingDates.put("checkout", checkOut);

        Map<String, Object> booking = new HashMap<>();
        booking.put("firstname", firstName);
        booking.put("lastname", lastName);
        booking.put("totalprice", totalPrice);
        booking.put("depositpaid", depositPaid);
        booking.put("bookingdates", bookingDates);
        booking.put("additionalneeds", additionalNeeds);
        return booking;
    }
}
