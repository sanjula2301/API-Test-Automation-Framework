package com.fintech.api.tests;

import com.fintech.api.base.BaseTest;
import com.fintech.api.clients.CryptoApiClient;
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
@Feature("Crypto Market Data")
public class CryptoApiTests extends BaseTest {

    private final CryptoApiClient cryptoApi = new CryptoApiClient();

    // =====================
    // HAPPY PATH TESTS
    // =====================

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify API health check returns status 200")
    @Story("API Health")
    public void testApiHealthCheck() {
        Response response = cryptoApi.ping();

        assertThat(response.statusCode()).isEqualTo(200);
        String geckoSays = response.jsonPath().getString("gecko_says");
        assertThat(geckoSays).isNotBlank();
        System.out.println("API Health Check Passed: " + geckoSays);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Bitcoin price is returned in USD")
    @Story("Crypto Prices")
    public void testGetBitcoinPrice() {
        Response response = cryptoApi.simplePrice("bitcoin", "usd");

        assertThat(response.statusCode()).isEqualTo(200);
        Float bitcoinPrice = response.jsonPath().getFloat("bitcoin.usd");
        assertThat(bitcoinPrice).isNotNull();
        assertThat(bitcoinPrice).isGreaterThan(0);
        System.out.println("Bitcoin Price USD: " + bitcoinPrice);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify multiple crypto prices returned correctly")
    @Story("Crypto Prices")
    public void testGetMultipleCryptoPrices() {
        Response response = cryptoApi.simplePrice("bitcoin,ethereum,tether", "usd");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getFloat("bitcoin.usd")).isGreaterThan(0);
        assertThat(response.jsonPath().getFloat("ethereum.usd")).isGreaterThan(0);
        assertThat(response.jsonPath().getFloat("tether.usd")).isGreaterThan(0);
        System.out.println("Multiple Crypto Prices Test Passed");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify top 10 coins list is returned")
    @Story("Coin List")
    public void testGetTopCoins() {
        Response response = cryptoApi.topCoins("usd", 10, 1);

        assertThat(response.statusCode()).isEqualTo(200);
        int coinCount = response.jsonPath().getList("$").size();
        assertThat(coinCount).isEqualTo(10);
        System.out.println("Top 10 Coins returned: " + coinCount);
    }

    // =====================
    // NEGATIVE TESTS
    // =====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify invalid coin ID returns empty response or accepted rate-limit response")
    @Story("Negative Tests")
    public void testInvalidCoinId() {
        Response response = cryptoApi.simplePrice("invalidcoin99999", "usd");

        assertThat(response.statusCode()).isIn(200, 429);
        if (response.statusCode() == 200) {
            assertThat(response.getBody().asString()).isEqualTo("{}");
            System.out.println("Invalid Coin Test Passed - Empty response returned");
        } else {
            System.out.println("Rate limited - 429 received, test acknowledged");
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify invalid currency response is handled")
    @Story("Negative Tests")
    public void testInvalidCurrency() {
        Response response = cryptoApi.simplePrice("bitcoin", "invalidcurrency");

        assertThat(response.statusCode()).isIn(200, 429);
        System.out.println("Invalid Currency Response Status: " + response.statusCode());
    }

    // =====================
    // RESPONSE VALIDATION
    // =====================

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify response time is under 5 seconds")
    @Story("Performance")
    public void testResponseTime() {
        Response response = cryptoApi.simplePrice("bitcoin", "usd");

        assertThat(response.statusCode()).isIn(200, 429);
        long responseTime = response.getTime();
        assertThat(responseTime).isLessThan(5000);
        System.out.println("Response Time: " + responseTime + "ms - Status: "
            + response.statusCode());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify content type is JSON")
    @Story("Response Validation")
    public void testContentTypeIsJson() {
        Response response = cryptoApi.simplePrice("bitcoin", "usd");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.contentType()).contains("application/json");
        System.out.println("Content Type Validation Passed");
    }
}
