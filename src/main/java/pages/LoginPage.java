package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.ConfigReader;

/**
 * Page Object for the Login page of the Munster Joinery Sandbox SUT.
 *
 * <p>Encapsulates all locators and user interactions related to authentication,
 * including valid login, invalid login, error message verification, and navigation
 * to the account recovery flow.</p>
 *
 * <p>Satisfies requirements R3, R4, R5.</p>
 */
public class LoginPage extends BasePage {

    // -------------------------------------------------------------------------
    // Locators — broad selectors to handle Angular/SPA apps where id may be absent
    // -------------------------------------------------------------------------

    // Username: Angular Material input with formControlName="username"
    private static final By USERNAME_FIELD =
            By.cssSelector("input[formcontrolname='username']");

    // Password: Angular Material input with formControlName="password"
    private static final By PASSWORD_FIELD =
            By.cssSelector("input[formcontrolname='password']");

    // Login button: the submit button with text "LOGIN"
    private static final By LOGIN_BUTTON =
            By.cssSelector("button[type='submit']");

    // Error message: the app shows login errors as a toast with class "toast-item danger-toast"
    private static final By LOGIN_ERROR_MESSAGE =
            By.cssSelector(".toast-item.danger-toast, .toast-item, [role='alert'].toast-item");

    // Forgotten Account Detail: button with class forgottenCustomBtn
    private static final By FORGOTTEN_ACCOUNT_LINK =
            By.cssSelector("button.forgottenCustomBtn");

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Constructs a LoginPage using the supplied {@link WebDriver}.
     *
     * @param driver the active WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // -------------------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------------------

    /**
     * Navigates the browser to the base URL configured in {@code testdata.properties}.
     *
     * @return this {@link LoginPage} instance for method chaining
     */
    public LoginPage open() {
        driver.get(ConfigReader.get("base.url"));
        return this;
    }

    /**
     * Clears the username field and types the supplied value.
     * Waits up to 10 s for the field to be visible (Angular SPA may render late).
     *
     * @param username the username to enter
     */
    public void enterUsername(String username) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_FIELD));
        field.clear();
        field.sendKeys(username);
    }

    /**
     * Clears the password field and types the supplied value.
     * Waits up to 10 s for the field to be visible.
     *
     * @param password the password to enter
     */
    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
        field.clear();
        field.sendKeys(password);
    }

    /**
     * Clicks the login button and waits for the browser to land on the dashboard
     * or property page (the SUT may redirect to either after a successful login).
     *
     * @return a new {@link DashboardPage} instance
     * @throws AssertionError if the expected URL is not reached within 10 seconds
     */
    public DashboardPage submitValidLogin() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        btn.click();
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlMatches(".*#/dashboard.*"),
                    ExpectedConditions.urlMatches(".*#/property.*")
            ));
        } catch (TimeoutException e) {
            throw new AssertionError("Dashboard not reached within timeout after valid login");
        }
        // Give Angular a moment to finish rendering the page components
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
        return new DashboardPage(driver);
    }

    /**
     * Clicks the login button without waiting for a URL change (used when
     * invalid credentials are expected to keep the user on the login page).
     *
     * @return this {@link LoginPage} instance for method chaining
     */
    public LoginPage submitInvalidLogin() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
        btn.click();
        return this;
    }

    /**
     * Waits up to 5 seconds for the login error message to become visible.
     *
     * @return {@code true} when the error message is visible
     * @throws AssertionError if the error message is not visible within 5 seconds
     */
    public boolean isErrorMessageDisplayed() {
        try {
            // Use full 10s wait — Angular apps may take time to render error feedback
            wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ERROR_MESSAGE));
        } catch (TimeoutException e) {
            throw new AssertionError(
                    "Error message not visible within timeout after invalid credentials submission");
        }
        return true;
    }

    /**
     * Returns the trimmed text content of the login error message element.
     *
     * <p>Call {@link #isErrorMessageDisplayed()} first to ensure the element is
     * present before reading its text.</p>
     *
     * @return the error message text
     */
    public String getErrorMessageText() {
        return driver.findElement(LOGIN_ERROR_MESSAGE).getText().trim();
    }

    /**
     * Clicks the "Forgotten" account link and returns the resulting
     * {@link RecoveryPage}.
     *
     * @return a new {@link RecoveryPage} instance
     */
    public RecoveryPage clickForgottenAccountLink() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(FORGOTTEN_ACCOUNT_LINK));
        link.click();
        return new RecoveryPage(driver);
    }
}
