package com.fintech.api.clients;

import com.fintech.api.config.FrameworkConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static com.fintech.api.core.ApiRequestFactory.apiSpec;
import static com.fintech.api.core.ApiRequestFactory.jsonApiSpec;
import static io.restassured.RestAssured.given;

public class BookingApiClient {

    private final RequestSpecification spec = apiSpec(FrameworkConfig.bookingBaseUrl());
    private final RequestSpecification jsonSpec = jsonApiSpec(FrameworkConfig.bookingBaseUrl());

    public Response authenticate(Map<String, String> credentials) {
        return given(jsonSpec)
            .body(credentials)
            .when()
            .post("/auth")
            .then()
            .extract().response();
    }

    public Response createBooking(Map<String, Object> booking) {
        return given(jsonSpec)
            .body(booking)
            .when()
            .post("/booking")
            .then()
            .extract().response();
    }

    public Response getAllBookings() {
        return given(spec)
            .when()
            .get("/booking")
            .then()
            .extract().response();
    }

    public Response getBooking(int bookingId) {
        return given(spec)
            .when()
            .get("/booking/" + bookingId)
            .then()
            .extract().response();
    }

    public Response updateBooking(int bookingId, String token, Map<String, Object> booking) {
        return given(jsonSpec)
            .header("Cookie", "token=" + token)
            .body(booking)
            .when()
            .put("/booking/" + bookingId)
            .then()
            .extract().response();
    }

    public Response updateBookingWithoutAuth(int bookingId, Map<String, Object> booking) {
        return given(jsonSpec)
            .body(booking)
            .when()
            .put("/booking/" + bookingId)
            .then()
            .extract().response();
    }

    public Response deleteBooking(int bookingId) {
        return given(spec)
            .when()
            .delete("/booking/" + bookingId)
            .then()
            .extract().response();
    }

    public Response deleteBooking(int bookingId, String token) {
        return given(spec)
            .header("Cookie", "token=" + token)
            .when()
            .delete("/booking/" + bookingId)
            .then()
            .extract().response();
    }
}
