package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the "Find a Property (Ireland)" dialog.
 *
 * The dialog is opened by clicking the "Find a Property" button on the dashboard.
 * All search fields use Angular reactive-form formcontrolname attributes:
 *   contactName, address, fittingNo, spacebarNo,
 *   serviceQueryNoServiceCallOutNo, builderAfterCareId,
 *   contactPhone, contactEmail, quoteId
 *
 * @author Maribel Aiza
 */
public class PropertySearchPage extends BasePage {

    // -------------------------------------------------------------------------
    // Button to open the dialog (on the dashboard toolbar)
    // -------------------------------------------------------------------------
    private static final By FIND_PROPERTY_BUTTON =
            By.cssSelector("button.my-2, button.mat-primary.my-2");

    // -------------------------------------------------------------------------
    // Dialog container
    // -------------------------------------------------------------------------
    private static final By DIALOG_CONTAINER =
            By.cssSelector("mat-dialog-container");

    // -------------------------------------------------------------------------
    // Search fields inside the dialog (formcontrolname from DOM inspection)
    // -------------------------------------------------------------------------
    private static final By NAME_FIELD =
            By.cssSelector("input[formcontrolname='contactName']");

    private static final By ADDRESS_FIELD =
            By.cssSelector("input[formcontrolname='address']");

    private static final By FITTING_NO_FIELD =
            By.cssSelector("input[formcontrolname='fittingNo']");

    private static final By ORDER_NO_FIELD =
            By.cssSelector("input[formcontrolname='spacebarNo']");

    private static final By SERVICE_NO_FIELD =
            By.cssSelector("input[formcontrolname='serviceQueryNoServiceCallOutNo']");

    private static final By BUILDER_AFTER_CARE_ID_FIELD =
            By.cssSelector("input[formcontrolname='builderAfterCareId']");

    private static final By PHONE_FIELD =
            By.cssSelector("input[formcontrolname='contactPhone']");

    private static final By EMAIL_FIELD =
            By.cssSelector("input[formcontrolname='contactEmail']");

    private static final By QUOTE_ID_FIELD =
            By.cssSelector("input[formcontrolname='quoteId']");

    // -------------------------------------------------------------------------
    // Dialog action buttons
    // -------------------------------------------------------------------------
    // Matches any button whose full text contains 'Search' (case-sensitive)
    // Excludes the 'Find a Property' button which also contains no 'Search' text
    private static final By SEARCH_BUTTON =
            By.xpath("//button[.//span[contains(normalize-space(text()),'Search')] or normalize-space(text())='Search']");

    private static final By CLEAR_BUTTON =
            By.xpath("//button[.//span[contains(normalize-space(text()),'Clear')] or normalize-space(text())='Clear']");

    // -------------------------------------------------------------------------
    // Results (ag-Grid rows rendered in the main page behind the dialog,
    // or inside the dialog after search)
    // -------------------------------------------------------------------------
    private static final By RESULT_ROWS =
            By.cssSelector(".ag-row, table tbody tr");

    private static final By NO_RECORDS_MESSAGE =
            By.xpath("//*[contains(text(),'No records') or contains(text(),'no records') or contains(@class,'ag-overlay-no-rows')]");

    public PropertySearchPage(WebDriver driver) {
        super(driver);
    }

    // -------------------------------------------------------------------------
    // Dialog lifecycle
    // -------------------------------------------------------------------------

    /** Ensures the search form is ready for input.
     *  On the #/property page the form is inline — no button click needed.
     *  On the #/dashboard page a "Find a Property" button opens a mat-dialog.
     */
    public void openDialog() {
        // Check if the form fields are already visible (inline form on #/property page)
        if (isDisplayed(NAME_FIELD)) {
            // Form is already open — just wait for it to be fully interactive
            wait.until(ExpectedConditions.elementToBeClickable(NAME_FIELD));
            return;
        }

        // Otherwise, click the "Find a Property" button to open the dialog
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(FIND_PROPERTY_BUTTON));
        btn.click();

        // Wait up to 15 s for the dialog's name input to appear
        WebDriverWait longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        try {
            longWait.until(ExpectedConditions.visibilityOfElementLocated(NAME_FIELD));
        } catch (TimeoutException e) {
            // Retry with JavaScript click in case the normal click was intercepted
            WebElement btn2 = driver.findElement(FIND_PROPERTY_BUTTON);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn2);
            longWait.until(ExpectedConditions.visibilityOfElementLocated(NAME_FIELD));
        }
    }

    /** Returns true if the dialog is currently visible. */
    public boolean isDialogOpen() {
        return isDisplayed(DIALOG_CONTAINER);
    }

    // -------------------------------------------------------------------------
    // Field helpers
    // -------------------------------------------------------------------------

    private void fillField(By locator, String value) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        // Use JavaScript to set value — handles Angular Material inputs that may be
        // temporarily or permanently disabled/read-only on certain form states.
        // Dispatching 'input' and 'change' events ensures Angular's reactive form picks up the value.
        ((JavascriptExecutor) driver).executeScript(
                "var el = arguments[0];" +
                "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "nativeInputValueSetter.call(el, arguments[1]);" +
                "el.dispatchEvent(new Event('input', {bubbles:true}));" +
                "el.dispatchEvent(new Event('change', {bubbles:true}));",
                field, value);
    }

    /** Fills a field using native sendKeys — use this when the field is enabled and
     *  Angular's reactive form must track the value (e.g. before clicking Clear). */
    private void fillFieldNative(By locator, String value) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));
        field.clear();
        field.sendKeys(value);
    }

    private void clickSearch() {
        // Use JavaScript to find and click the Search button — avoids XPath text matching issues
        // with Angular Material buttons where text may be in nested spans with whitespace
        Boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var btns = Array.from(document.querySelectorAll('button'));" +
                "var btn = btns.find(b => b.textContent.trim().toUpperCase().includes('SEARCH'));" +
                "if (btn) { btn.click(); return true; } return false;");
        if (!Boolean.TRUE.equals(clicked)) {
            // Log all button texts to help diagnose
            String allButtons = (String) ((JavascriptExecutor) driver).executeScript(
                    "return Array.from(document.querySelectorAll('button'))" +
                    ".map(b => b.textContent.trim()).join(' | ');");
            throw new RuntimeException("Search button not found. All buttons: " + allButtons);
        }
    }

    private void clickClear() {
        Boolean clicked = (Boolean) ((JavascriptExecutor) driver).executeScript(
                "var btns = Array.from(document.querySelectorAll('button'));" +
                "var btn = btns.find(b => b.textContent.trim().toUpperCase().includes('CLEAR'));" +
                "if (btn) { btn.click(); return true; } return false;");
        if (!Boolean.TRUE.equals(clicked)) {
            throw new RuntimeException("Clear button not found on page.");
        }
        // Wait for Angular to process the form reset
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    // -------------------------------------------------------------------------
    // Single-field searches
    // -------------------------------------------------------------------------

    public void searchByName(String name) {
        fillField(NAME_FIELD, name);
        clickSearch();
    }

    public void searchByPostCode(String postCode) {
        fillField(ADDRESS_FIELD, postCode);
        clickSearch();
    }

    public void searchByFittingNo(String fittingNo) {
        fillField(FITTING_NO_FIELD, fittingNo);
        clickSearch();
    }

    public void searchByOrderNo(String orderNo) {
        fillField(ORDER_NO_FIELD, orderNo);
        clickSearch();
    }

    public void searchByServiceNo(String serviceNo) {
        fillField(SERVICE_NO_FIELD, serviceNo);
        clickSearch();
    }

    public void searchByBuilderAfterCareId(String id) {
        fillField(BUILDER_AFTER_CARE_ID_FIELD, id);
        clickSearch();
    }

    public void searchByPhone(String phone) {
        fillField(PHONE_FIELD, phone);
        clickSearch();
    }

    public void searchByEmail(String email) {
        fillField(EMAIL_FIELD, email);
        clickSearch();
    }

    public void searchByQuoteId(String quoteId) {
        fillField(QUOTE_ID_FIELD, quoteId);
        clickSearch();
    }

    // -------------------------------------------------------------------------
    // Multi-field searches
    // -------------------------------------------------------------------------

    /** Fills name + postCode then clicks Search. */
    public void searchByNameAndPostCode(String name, String postCode) {
        fillField(NAME_FIELD, name);
        fillField(ADDRESS_FIELD, postCode);
        clickSearch();
    }

    /** Fills name + postCode + fittingNo then clicks Search. */
    public void searchByMultipleFields(String name, String postCode, String fittingNo) {
        fillField(NAME_FIELD, name);
        fillField(ADDRESS_FIELD, postCode);
        fillField(FITTING_NO_FIELD, fittingNo);
        clickSearch();
    }

    // -------------------------------------------------------------------------
    // Clear
    // -------------------------------------------------------------------------

    /** Fills name and address fields using native sendKeys (so Angular tracks the values),
     *  then clicks Clear. Used by TC13 to verify the Clear button resets the form. */
    public void fillAndClear(String name, String address) {
        fillFieldNative(NAME_FIELD, name);
        fillFieldNative(ADDRESS_FIELD, address);
        clickClear();
    }

    public String getNameFieldValue() {
        WebElement el = driver.findElement(NAME_FIELD);
        String val = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].value;", el);
        return val != null ? val : "";
    }

    public String getAddressFieldValue() {
        WebElement el = driver.findElement(ADDRESS_FIELD);
        String val = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].value;", el);
        return val != null ? val : "";
    }

    public String getFieldValue(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String val = (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].value;", el);
        return val != null ? val : "";
    }

    public String getFieldMaxLength(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String maxLen = el.getAttribute("maxlength");
        return maxLen != null ? maxLen : "";
    }

    public String getFieldType(By locator) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        String type = el.getAttribute("type");
        return type != null ? type : "text";
    }

    public boolean isFieldVisible(By locator) {
        return isDisplayed(locator);
    }

    // Expose field locators for validation tests
    public static By nameFieldLocator()           { return NAME_FIELD; }
    public static By addressFieldLocator()        { return ADDRESS_FIELD; }
    public static By fittingNoFieldLocator()      { return FITTING_NO_FIELD; }
    public static By orderNoFieldLocator()        { return ORDER_NO_FIELD; }
    public static By serviceNoFieldLocator()      { return SERVICE_NO_FIELD; }
    public static By builderAfterCareIdLocator()  { return BUILDER_AFTER_CARE_ID_FIELD; }
    public static By phoneFieldLocator()          { return PHONE_FIELD; }
    public static By emailFieldLocator()          { return EMAIL_FIELD; }
    public static By quoteIdFieldLocator()        { return QUOTE_ID_FIELD; }

    /** Sets a field value and returns what the field actually contains after input.
     *  Used for validation tests to check if the app accepts or rejects the value. */
    public String setFieldAndGetValue(By locator, String value) {
        fillField(locator, value);
        return getFieldValue(locator);
    }

    // -------------------------------------------------------------------------
    // Results
    // -------------------------------------------------------------------------

    /** Waits up to 10 s for at least one result row to become visible. */
    public void waitForResults() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(RESULT_ROWS));
    }

    public List<WebElement> getResultRows() {
        return driver.findElements(RESULT_ROWS);
    }

    public int getResultCount() {
        return getResultRows().size();
    }

    public boolean isNoRecordsMessageDisplayed() {
        return isDisplayed(NO_RECORDS_MESSAGE);
    }

    // -------------------------------------------------------------------------
    // Cell accessors (ag-Grid cells by column index)
    // -------------------------------------------------------------------------

    private String getCellText(WebElement row, int colIndex) {
        List<WebElement> cells = row.findElements(By.cssSelector(".ag-cell, td"));
        if (cells.size() > colIndex) {
            WebElement cell = cells.get(colIndex);
            // ag-Grid renders text inside nested spans — try innerText via JS first
            String text = cell.getText().trim();
            if (text.isEmpty()) {
                text = (String) ((JavascriptExecutor) driver)
                        .executeScript("return arguments[0].innerText;", cell);
                if (text != null) text = text.trim();
            }
            return text != null ? text : "";
        }
        return "";
    }

    public String getNameCellText(WebElement row)      { return getCellText(row, 0); }
    public String getAddressCellText(WebElement row)   { return getCellText(row, 1); }
    public String getFittingNoCellText(WebElement row) { return getCellText(row, 2); }
    public String getOrderNoCellText(WebElement row)   { return getCellText(row, 3); }
}
