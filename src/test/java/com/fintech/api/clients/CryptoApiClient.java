package com.fintech.api.clients;

import com.fintech.api.config.FrameworkConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static com.fintech.api.core.ApiRequestFactory.apiSpec;
import static io.restassured.RestAssured.given;

public class CryptoApiClient {

    private final RequestSpecification spec = apiSpec(FrameworkConfig.cryptoBaseUrl());

    public Response ping() {
        return given(spec)
            .when()
            .get("/ping")
            .then()
            .extract().response();
    }

    public Response simplePrice(String ids, String currencies) {
        return given(spec)
            .queryParam("ids", ids)
            .queryParam("vs_currencies", currencies)
            .when()
            .get("/simple/price")
            .then()
            .extract().response();
    }

    public Response topCoins(String currency, int perPage, int page) {
        return given(spec)
            .queryParam("vs_currency", currency)
            .queryParam("order", "market_cap_desc")
            .queryParam("per_page", perPage)
            .queryParam("page", page)
            .when()
            .get("/coins/markets")
            .then()
            .extract().response();
    }
}
