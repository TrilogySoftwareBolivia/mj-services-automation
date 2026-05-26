# Munster Joinery UI Automation Framework

Selenium/TestNG UI automation framework for the Munster Joinery Sandbox application. It follows the Page Object Model pattern and generates self-contained HTML reports via ExtentReports.

---

## Prerequisites

- Java 11 or higher
- Maven 3.8 or higher
- Supported browsers: Chrome, Firefox, Edge (Chrome is the default)

---

## Setup

1. Clone the repository.

2. Configure `src/test/resources/testdata.properties` with your credentials and test data.

3. The following keys are required in that file:

   | Key | Description |
   |-----|-------------|
   | `login.username` | Valid username for the SUT |
   | `login.password` | Valid password for the SUT |
   | `login.invalid.username` | Username that should be rejected (default: `invalid_user@test.com`) |
   | `login.invalid.password` | Password that should be rejected (default: `WrongPass123`) |
   | `dashboard.title` | Expected substring of the page title after login (default: `Munster Joinery`) |
   | `search.customerName` | Customer/builder name known to exist in the SUT |
   | `search.postCode` | Post code known to exist in the SUT |
   | `search.fittingNo` | Fitting number known to exist in the SUT (1–20 alphanumeric chars) |
   | `search.orderNo` | Order number known to exist in the SUT |
   | `base.url` | Base URL of the SUT (default: `https://is.munsterjoinery.ie:9099/mj-sandbox/#/`) |

---

## Environment variable override

Environment variables take precedence over values in `testdata.properties`. The convention is to uppercase the key and replace dots with underscores.

Examples:
- `login.username` → `LOGIN_USERNAME`
- `login.password` → `LOGIN_PASSWORD`
- `search.customerName` → `SEARCH_CUSTOMERNAME`

Set the variable in your shell or CI environment before running the tests and it will override whatever is in the properties file.

---

## Running the tests

```bash
mvn clean test
```

---

## Test cases

| ID | Description |
|----|-------------|
| TC01 | Valid login — verifies that a user with correct credentials reaches the dashboard |
| TC02 | Invalid login — verifies that incorrect credentials display an error message |
| TC03 | Forgotten account — verifies that the account-recovery form is displayed after clicking the link |
| TC04 | Search by customer name — verifies that results are returned for a known customer name |
| TC05 | Search by post code — verifies that results are returned for a known post code |
| TC06 | Search by fitting number — verifies that exactly one result is returned for a known fitting number |
| TC07 | Search by order number — verifies that results are returned for a known order number |

---

## Reports

After a test run, the HTML report is written to:

```
target/reports/report_<YYYYMMDD>_<HHMMSS>.html
```

The file is self-contained (no external CSS or JavaScript dependencies) and can be opened directly in any browser. Screenshots are embedded as Base64 data URIs on test failure.

---

## Parallel execution

Open `testng.xml` and replace the `<suite>` opening tag with:

```xml
<suite name="MunsterJoinery-Suite" parallel="classes" thread-count="3">
```

`DriverManager` uses a `ThreadLocal<WebDriver>` so each thread gets its own isolated browser instance — no other code changes are needed.
