package com.fintech.api.core;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public final class ApiRequestFactory {

    private ApiRequestFactory() {
    }

    public static RequestSpecification apiSpec(String baseUrl) {
        return new RequestSpecBuilder()
            .setBaseUri(baseUrl)
            .addHeader("Accept", "application/json")
            .addHeader("User-Agent", "fintech-api-testing")
            .build();
    }

    public static RequestSpecification jsonApiSpec(String baseUrl) {
        return new RequestSpecBuilder()
            .addRequestSpecification(apiSpec(baseUrl))
            .setContentType("application/json")
            .build();
    }
}
