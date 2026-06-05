package com.fintech.api.tests;

import com.fintech.api.base.BaseTest;
import com.fintech.api.clients.ExchangeRateApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Fintech API Testing")
@Feature("Currency Exchange Rates")
public class ExchangeRateApiTests extends BaseTest {

    private final ExchangeRateApiClient exchangeRateApi = new ExchangeRateApiClient();

    // =====================
    // HAPPY PATH TESTS
    // =====================

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify USD base currency returns valid rates")
    @Story("Currency Exchange")
    public void testGetUSDExchangeRates() {
        Response response = exchangeRateApi.latestRates("USD");

        assertThat(response.statusCode()).isEqualTo(200);
        String result = response.jsonPath().getString("result");
        assertThat(result).isEqualTo("success");
        System.out.println("USD Exchange Rates Test Passed");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify AED exchange rate exists in USD response")
    @Story("Currency Exchange")
    public void testAEDRateExists() {
        Response response = exchangeRateApi.latestRates("USD");

        assertThat(response.statusCode()).isEqualTo(200);
        Float aedRate = response.jsonPath().getFloat("rates.AED");
        assertThat(aedRate).isNotNull();
        assertThat(aedRate).isGreaterThan(0);
        System.out.println("AED Rate: " + aedRate);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify EUR base currency returns valid rates")
    @Story("Currency Exchange")
    public void testGetEURExchangeRates() {
        Response response = exchangeRateApi.latestRates("EUR");

        assertThat(response.statusCode()).isEqualTo(200);
        String baseCode = response.jsonPath().getString("base_code");
        assertThat(baseCode).isEqualTo("EUR");
        System.out.println("EUR Base Currency Test Passed");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify AED base currency returns valid rates")
    @Story("Currency Exchange")
    public void testGetAEDExchangeRates() {
        Response response = exchangeRateApi.latestRates("AED");

        assertThat(response.statusCode()).isEqualTo(200);
        String baseCode = response.jsonPath().getString("base_code");
        Float usdRate = response.jsonPath().getFloat("rates.USD");
        assertThat(baseCode).isEqualTo("AED");
        assertThat(usdRate).isGreaterThan(0);
        System.out.println("AED Base Rate Test Passed - USD rate: " + usdRate);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify response contains time last updated field")
    @Story("Data Integrity")
    public void testResponseContainsTimeLastUpdated() {
        Response response = exchangeRateApi.latestRates("USD");

        assertThat(response.statusCode()).isEqualTo(200);
        Long timeLastUpdated = response.jsonPath().getLong("time_last_update_unix");
        assertThat(timeLastUpdated).isNotNull();
        assertThat(timeLastUpdated).isGreaterThan(0);
        System.out.println("Time Last Updated: " + timeLastUpdated);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify USD to AED rate is approximately 3.67")
    @Story("Data Integrity")
    public void testUSDtoAEDRateIsRealistic() {
        Response response = exchangeRateApi.latestRates("USD");

        assertThat(response.statusCode()).isEqualTo(200);
        Float aedRate = response.jsonPath().getFloat("rates.AED");
        assertThat(aedRate).isBetween(3.5f, 3.8f);
        System.out.println("USD to AED Rate Realistic Test Passed: " + aedRate);
    }

    // =====================
    // NEGATIVE TESTS
    // =====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify invalid currency code behavior is handled")
    @Story("Negative Tests")
    public void testInvalidCurrencyCode() {
        Response response = exchangeRateApi.latestRates("INVALID");

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            String result = response.jsonPath().getString("result");
            assertThat(result).isEqualTo("error");
            System.out.println("Invalid Currency - API returned 200 with error result: " + result);
        } else {
            assertThat(statusCode).isIn(400, 404, 422);
            System.out.println("Invalid Currency Code Test Passed - Status: " + statusCode);
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify response time is under 5 seconds")
    @Story("Performance")
    public void testResponseTimeUnder5Seconds() {
        Response response = exchangeRateApi.latestRates("USD");

        assertThat(response.statusCode()).isEqualTo(200);
        long responseTime = response.getTime();
        assertThat(responseTime).isLessThan(5000);
        System.out.println("Response Time: " + responseTime + "ms");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify content type is JSON")
    @Story("Response Validation")
    public void testContentTypeIsJson() {
        Response response = exchangeRateApi.latestRates("USD");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.contentType()).contains("application/json");
        System.out.println("Content Type JSON Validation Passed");
    }
}
