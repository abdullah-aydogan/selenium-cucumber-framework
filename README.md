# Enterprise Selenium-Cucumber Test Automation Framework

An enterprise-grade, scalable End-to-End (E2E) UI test automation framework built for web applications. This framework features a **JSON-based Element Repository**, a thread-safe **Parallel Execution Engine**, and integration with **Allure Reporting** and **GitHub Actions CI/CD**.

Target Application: [SauceDemo (Swag Labs)](https://www.saucedemo.com/)

---

## Key Highlights & Architectural Features

- **Page Object Model (POM):** Decoupled page logic and assertions ensuring high test maintenance and modularity.
- **Dynamic JSON Element Repository:** UI element locators (ID, XPath, CSS, etc.) are isolated from Java code in structured JSON files, supporting dynamic runtime formatting (`String.format`).
- **ThreadLocal WebDriver Management:** Fully thread-safe browser session management enabling concurrent multi-browser execution without race conditions.
- **Robust Synchronization:** Zero arbitrary sleeps (`Thread.sleep`). Explicit waits (`WebDriverWait` & `ExpectedConditions`) ensure fast, flake-free execution.
- **Automated Failure Screenshots:** Cucumber Hooks automatically capture full-screen screenshots on test failure and embed them into both Cucumber HTML and Allure lifecycle reports.
- **Centralized Logging:** Detailed contextual logging across all layers using **Log4j2** (console and rolling file outputs).
- **CI/CD Integration:** Automated headless pipeline execution on push/PR via **GitHub Actions**.

---

## Tech Stack & Dependencies

| Technology | Purpose |
| :--- | :--- |
| **Java 21** | Core programming language |
| **Selenium WebDriver (4.25.0)** | Browser automation engine |
| **Cucumber JVM (7.18.1)** | BDD Gherkin scenario definition |
| **TestNG (7.10.2)** | Test execution and parallel runner orchestration |
| **Jackson Databind** | High-performance JSON element parsing and caching |
| **Log4j2** | Enterprise logging framework |
| **Allure Reports** | Interactive test reporting with failure attachments |
| **Maven** | Build and dependency lifecycle manager |

---

## Project Directory Structure

```text
selenium-cucumber-framework/
├── .github/
│   └── workflows/
│       └── tests.yml                 # GitHub Actions CI workflow
├── src/
│   ├── main/
│   │   ├── java/com/automation/
│   │   │   ├── config/               # Configuration reader (properties loader)
│   │   │   ├── driver/               # ThreadLocal WebDriver factory
│   │   │   ├── pages/                # Page Object Model classes
│   │   │   └── utils/                # Dynamic JSON ElementReader engine
│   │   └── resources/
│   │       ├── config.properties     # Application and browser configurations
│   │       ├── log4j2.xml            # Log4j2 console and file pattern config
│   │       └── elements/             # JSON Element Repositories
│   │           ├── login_page.json
│   │           ├── products_page.json
│   │           ├── cart_page.json
│   │           └── checkout_page.json
│   └── test/
│       ├── java/com/automation/
│       │   ├── hooks/                # Cucumber setup/teardown & failure handlers
│       │   ├── runners/              # TestNG parallel cucumber runner
│       │   └── stepdefs/             # Step definition glue code
│       └── resources/
│           ├── features/             # Gherkin BDD feature specifications
│           │   ├── login.feature
│           │   └── checkout.feature
│           └── testng.xml            # TestNG suite definition
├── pom.xml
└── README.md
```

---

## How It Works: Dynamic JSON Locators

Instead of hardcoding `@FindBy` annotations in Java classes, locators are maintained inside `src/main/resources/elements/*.json`.

Example element definition (`products_page.json`):
```json
{
  "add_to_cart_by_product_name": {
    "type": "xpath",
    "value": "//div[text()='%s']/ancestor::div[@class='inventory_item']//button[contains(@data-test, 'add-to-cart')]"
  }
}
```

The `ElementReader` parses and caches the JSON tree, dynamically resolving it at runtime:
```java
// Resolves dynamically to the product name at runtime
click("products_page", "add_to_cart_by_product_name", "Sauce Labs Backpack");
```

---

## Getting Started

### Prerequisites
- **JDK 21** or higher installed.
- **Maven 3.8+** installed.
- Google Chrome (or Firefox / Edge) installed.

### Configuration
Update `src/main/resources/config.properties` if needed:
```properties
base.url=https://www.saucedemo.com/
browser=chrome
implicit.wait=0
explicit.wait=10
page.load.timeout=30
```

---

## Executing Tests

### 1. Run All Tests (Standard Headed Chrome)
```bash
mvn clean test
```

### 2. Run Tests in Headless Mode (CI / Background)
You can override the target browser directly from the CLI:
```bash
mvn clean test -Dbrowser=chrome-headless
```

### 3. Run Specific Scenarios by Cucumber Tags
```bash
# Run only smoke scenarios
mvn clean test -Dcucumber.filter.tags="@smoke"

# Run only authentication scenarios
mvn clean test -Dcucumber.filter.tags="@login"

# Run only checkout scenarios
mvn clean test -Dcucumber.filter.tags="@checkout"
```

---

## Test Reports & Logs

### 1. Cucumber Standard HTML Report
After execution, open the report in any browser:
```text
target/cucumber-reports/cucumber.html
```

### 2. Allure Interactive Report
Generate and open the Allure report locally:
```bash
# Requires Allure CLI installed (e.g. 'brew install allure' or scoop/choco on Windows)
allure serve allure-results
```

### 3. Execution Logs
Detailed logs with thread IDs and timestamps are written to:
```text
logs/automation.log
```