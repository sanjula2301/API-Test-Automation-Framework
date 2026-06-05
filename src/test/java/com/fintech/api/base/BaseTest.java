package com.fintech.api.base;

import com.fintech.api.config.FrameworkConfig;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class BaseTest {

    @BeforeSuite
    public void setUp() {
        int timeout = FrameworkConfig.apiTimeoutMillis();
        RestAssured.config = RestAssuredConfig.config()
            .encoderConfig(EncoderConfig.encoderConfig()
                .appendDefaultContentCharsetToContentTypeIfUndefined(false))
            .httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", timeout)
                .setParam("http.socket.timeout", timeout));

        if (FrameworkConfig.relaxedHttpsValidationEnabled()) {
            RestAssured.useRelaxedHTTPSValidation();
        }

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        copyAllureMetadata();
    }

    @BeforeMethod
    public void waitBetweenTests() {
        try {
            Thread.sleep(FrameworkConfig.testDelayMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void copyAllureMetadata() {
        copyResourceToAllureResults("environment.properties");
        copyResourceToAllureResults("categories.json");
    }

    private void copyResourceToAllureResults(String resourceName) {
        Path allureResults = Path.of(System.getProperty(
            "allure.results.directory",
            "target/allure-results"
        ));

        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (resource == null) {
                return;
            }
            Files.createDirectories(allureResults);
            Files.copy(resource, allureResults.resolve(resourceName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to copy Allure metadata: " + resourceName, e);
        }
    }
}
