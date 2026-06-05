# Fintech API Testing Framework

Production-style API automation framework using REST Assured, TestNG, AssertJ, and Allure.

## What It Covers

- Crypto market API tests
- Currency exchange API tests
- Booking API authentication and CRUD tests
- Allure reporting with suites, stories, severities, categories, and environment metadata
- Configurable base URLs, timeout, and test delay through Maven system properties
- Configurable relaxed HTTPS validation for local machines with certificate trust issues

## Run Tests

```powershell
mvn clean test
```

## Open Allure Report

```powershell
allure serve target/allure-results
```

## Override Runtime Config

```powershell
mvn clean test `
  -Dcrypto.base.url=https://api.coingecko.com/api/v3 `
  -Dexchange.base.url=https://open.er-api.com/v6 `
  -Dbooking.base.url=https://restful-booker.herokuapp.com `
  -Dapi.test.delay.ms=2000 `
  -Dapi.timeout.ms=10000 `
  -Dapi.relaxed.https=true
```

For real production or CI environments, prefer importing the correct CA certificate into Java and run with `-Dapi.relaxed.https=false`.

## Framework Structure

- `base`: shared TestNG setup, timeouts, Allure metadata
- `clients`: reusable API clients per service
- `config`: runtime configuration
- `core`: REST Assured request specification factory
- `testdata`: reusable request payload builders
- `tests`: readable business-level test scenarios
