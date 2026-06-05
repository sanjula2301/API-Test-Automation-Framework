package com.fintech.api.tests;

import com.fintech.api.base.BaseTest;
import com.fintech.api.clients.BookingApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static com.fintech.api.testdata.BookingTestData.booking;
import static com.fintech.api.testdata.BookingTestData.invalidCredentials;
import static com.fintech.api.testdata.BookingTestData.validCredentials;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("Fintech API Testing")
@Feature("Booking API Auth and CRUD")
public class RestfulBookerApiTests extends BaseTest {

    private final BookingApiClient bookingApi = new BookingApiClient();
    private int bookingId;

    private String getFreshToken() {
        Response response = bookingApi.authenticate(validCredentials());

        assertThat(response.statusCode()).isEqualTo(200);
        String token = response.jsonPath().getString("token");
        assertThat(token).isNotBlank();
        System.out.println("Fresh token obtained: " + token);
        return token;
    }

    // =====================
    // TEST DATA SETUP
    // =====================

    @BeforeClass
    public void createInitialBooking() {
        Map<String, Object> initialBooking = booking(
            "Sanjula",
            "Hewage",
            150,
            true,
            "2025-01-01",
            "2025-01-10",
            "Breakfast"
        );

        Response response = bookingApi.createBooking(initialBooking);

        assertThat(response.statusCode()).isEqualTo(200);
        bookingId = response.jsonPath().getInt("bookingid");
        assertThat(bookingId).isGreaterThan(0);
        System.out.println("Test booking created with ID: " + bookingId);
    }

    // =====================
    // AUTH TESTS
    // =====================

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify authentication returns valid token")
    @Story("Authentication")
    public void testAuthTokenIsValid() {
        String token = getFreshToken();

        assertThat(token.length()).isGreaterThan(5);
        System.out.println("Token Validation Passed: " + token);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify invalid credentials return bad credentials message")
    @Story("Authentication")
    public void testInvalidCredentials() {
        Response response = bookingApi.authenticate(invalidCredentials());

        assertThat(response.statusCode()).isEqualTo(200);
        String reason = response.jsonPath().getString("reason");
        assertThat(reason).isEqualTo("Bad credentials");
        System.out.println("Invalid Credentials Test Passed");
    }

    // =====================
    // CREATE TESTS
    // =====================

    @Test(priority = 3)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify creating a new booking returns booking ID")
    @Story("Create Booking")
    public void testCreateBooking() {
        Map<String, Object> newBooking = booking(
            "Test",
            "Create",
            200,
            true,
            "2025-03-01",
            "2025-03-10",
            "Dinner"
        );

        Response response = bookingApi.createBooking(newBooking);

        assertThat(response.statusCode()).isEqualTo(200);
        int newBookingId = response.jsonPath().getInt("bookingid");
        assertThat(newBookingId).isGreaterThan(0);
        assertThat(response.jsonPath().getString("booking.firstname")).isEqualTo("Test");
        System.out.println("Booking Created - ID: " + newBookingId);
    }

    // =====================
    // READ TESTS
    // =====================

    @Test(priority = 4)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify getting all bookings returns a list")
    @Story("Read Bookings")
    public void testGetAllBookings() {
        Response response = bookingApi.getAllBookings();

        assertThat(response.statusCode()).isEqualTo(200);
        int bookingCount = response.jsonPath().getList("$").size();
        assertThat(bookingCount).isGreaterThan(0);
        System.out.println("Total Bookings Found: " + bookingCount);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify getting booking by ID returns correct data")
    @Story("Read Bookings")
    public void testGetBookingById() {
        Response response = bookingApi.getBooking(bookingId);

        assertThat(response.statusCode()).isEqualTo(200);
        String firstName = response.jsonPath().getString("firstname");
        assertThat(firstName).isEqualTo("Sanjula");
        System.out.println("Get Booking By ID Passed - Name: " + firstName);
    }

    // =====================
    // UPDATE TESTS
    // =====================

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify updating a booking with fresh token succeeds")
    @Story("Update Booking")
    public void testUpdateBooking() {
        String freshToken = getFreshToken();
        Map<String, Object> updatedBooking = booking(
            "Sanjula",
            "Updated",
            200,
            true,
            "2025-02-01",
            "2025-02-10",
            "Lunch"
        );

        Response response = bookingApi.updateBooking(bookingId, freshToken, updatedBooking);

        assertThat(response.statusCode()).isEqualTo(200);
        String lastName = response.jsonPath().getString("lastname");
        assertThat(lastName).isEqualTo("Updated");
        System.out.println("Update Booking Test Passed");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating booking without token returns 403")
    @Story("Update Booking")
    public void testUpdateBookingWithoutAuth() {
        Map<String, Object> unauthorizedBooking = booking(
            "Hacker",
            "Attempt",
            0,
            false,
            "2025-03-01",
            "2025-03-10",
            "None"
        );

        Response response = bookingApi.updateBookingWithoutAuth(bookingId, unauthorizedBooking);

        assertThat(response.statusCode()).isEqualTo(403);
        System.out.println("Unauthorized Update Correctly Blocked - 403");
    }

    // =====================
    // DELETE TESTS
    // =====================

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify deleting booking without auth returns 403")
    @Story("Delete Booking")
    public void testDeleteBookingWithoutAuth() {
        Response response = bookingApi.deleteBooking(bookingId);

        assertThat(response.statusCode()).isEqualTo(403);
        System.out.println("Unauthorized Delete Correctly Blocked - 403");
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify deleting booking with fresh token succeeds")
    @Story("Delete Booking")
    public void testDeleteBooking() {
        String freshToken = getFreshToken();

        Response response = bookingApi.deleteBooking(bookingId, freshToken);

        assertThat(response.statusCode()).isEqualTo(201);
        System.out.println("Booking Deleted Successfully - ID: " + bookingId);
    }

    @Test(priority = 10)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify deleted booking returns 404")
    @Story("Delete Booking")
    public void testGetDeletedBookingReturns404() {
        Response response = bookingApi.getBooking(bookingId);

        assertThat(response.statusCode()).isEqualTo(404);
        System.out.println("Deleted Booking Returns 404 - Confirmed");
    }
}
