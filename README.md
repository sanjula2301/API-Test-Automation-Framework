# Fintech API Test Automation Framework

![CI](https://github.com/sanjula2301/API-Test-Automation-Framework/actions/workflows/api-tests.yml/badge.svg)
![Java](https://img.shields.io/badge/Java-17-orange)
![RestAssured](https://img.shields.io/badge/RestAssured-5.3.2-green)
![TestNG](https://img.shields.io/badge/TestNG-7.8.0-blue)
![Allure](https://img.shields.io/badge/Allure-2.24.0-yellow)

A professional API test automation framework built with Java, RestAssured, 
and TestNG — covering fintech-relevant APIs including cryptocurrency market 
data, currency exchange, and authenticated booking workflows.

Built as part of a QA portfolio demonstrating real-world API testing skills 
relevant to fintech and enterprise platforms.

---

## Tech Stack

| Tool | Purpose |
|---|---|
| Java 17 | Programming language |
| RestAssured 5.3.2 | API test automation library |
| TestNG 7.8.0 | Test runner and suite management |
| Allure Reports 2.24.0 | Visual test reporting |
| Maven | Build and dependency management |
| GitHub Actions | CI/CD pipeline — runs on every push |

---

## APIs Tested

### 1. CoinGecko API — Cryptocurrency Market Data
Relevant to asset tokenisation and crypto trading platforms

- API health check and availability
- Bitcoin, Ethereum, Tether price validation
- Multiple cryptocurrency price retrieval
- Top 10 coins by market cap
- Invalid coin ID handling
- Invalid currency handling
- Response time performance validation
- Content type validation

### 2. ExchangeRate API — Currency Exchange
Relevant to payment processing and multi-currency fintech platforms

- USD base currency rate retrieval
- AED exchange rate validation (UAE market)
- EUR and AED base currency switching
- Data freshness validation (last updated timestamp)
- USD to AED peg validation (3.6725)
- Invalid currency code handling
- Response time and content type validation

### 3. RestfulBooker API — Authentication and CRUD Operations
Relevant to any platform requiring authenticated API workflows

- Token-based authentication flow
- Invalid credentials handling
- Full CRUD — Create, Read, Update, Delete
- Unauthorized access blocked (403 validation)
- Deleted resource returns 404
- Token lifecycle management

---

## Test Coverage

| Category | Count |
|---|---|
| Happy Path Tests | 14 |
| Negative Tests | 7 |
| Security / Auth Tests | 4 |
| Performance Tests | 2 |
| **Total** | **27** |

---

## Framework Features

- **Page Object style architecture** — BaseTest pattern for shared setup
- **Rate limit handling** — automatic delays between requests
- **Token lifecycle management** — fresh token per authenticated request
- **Full request/response logging** — every API call logged for debugging
- **Allure Reports** — visual HTML dashboard with pass/fail details
- **GitHub Actions CI/CD** — pipeline runs automatically on every push
- **Cross-environment ready** — base URLs configurable per environment

---

## Project Structure
```
fintech-api-testing/
│
├── src/test/java/com/fintech/api/
│   ├── base/
│   │   └── BaseTest.java          # Shared setup, logging, delays
│   ├── tests/
│   │   ├── CryptoApiTests.java    # CoinGecko API tests
│   │   ├── ExchangeRateApiTests.java  # Currency exchange tests
│   │   └── RestfulBookerApiTests.java # Auth and CRUD tests
│
├── src/test/resources/
│   └── testng.xml                 # Test suite configuration
│
├── .github/workflows/
│   └── api-tests.yml              # GitHub Actions CI/CD pipeline
│
└── pom.xml                        # Dependencies and build config
```

---

## How To Run Locally

**Prerequisites:** Java 17+, Maven 3.9+

**Clone the repo:**
```bash
git clone https://github.com/sanjula2301/API-Test-Automation-Framework.git
cd API-Test-Automation-Framework
```

**Run all tests:**
```bash
mvn test
```

**Generate Allure report:**
```bash
allure serve target/allure-results
```

---

## CI/CD Pipeline

Every push to main branch automatically:
1. Sets up Java 17 environment
2. Caches Maven dependencies
3. Runs all 27 API tests
4. Generates Allure report
5. Uploads report as downloadable artifact

---

## Key Challenges Solved

**Rate Limiting (HTTP 429)** — CoinGecko free tier limits requests per 
minute. Solved by implementing 2-second delays between tests using 
TestNG BeforeMethod, and building resilient assertions that handle 
rate limit responses gracefully.

**Token Expiry** — RestfulBooker auth tokens expire during long test 
runs. Solved by implementing a getFreshToken() helper method that 
generates a new token immediately before each authenticated operation.

---

## Author

**Sanjula Hewage** — QA Engineer | SDET | Dubai, UAE

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue)](https://www.linkedin.com/in/sanjulahewage/)
