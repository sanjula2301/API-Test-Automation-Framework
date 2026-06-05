package com.fintech.api.clients;

import com.fintech.api.config.FrameworkConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.fintech.api.core.ApiRequestFactory.apiSpec;
import static io.restassured.RestAssured.given;

public class ExchangeRateApiClient {

    private final RequestSpecification spec = apiSpec(FrameworkConfig.exchangeBaseUrl());

    public Response latestRates(String currencyCode) {
        return given(spec)
            .when()
            .get("/latest/" + currencyCode)
            .then()
            .extract().response();
    }
}
