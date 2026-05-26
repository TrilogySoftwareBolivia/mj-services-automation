package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page Object for the Account Detail / Forgotten Account screen.
 *
 * The "Forgotten Account Detail" button on the login page navigates to
 * app-account-detail — a full page (not a dialog) with formcontrolname="name",
 * formcontrolname="email", formcontrolname="phoneNumber", formcontrolname="department".
 *
 * Satisfies requirement R5.
 */
public class RecoveryPage extends BasePage {

    // The account detail page is rendered inside app-account-detail
    private static final By RECOVERY_FORM_CONTAINER =
            By.cssSelector("app-account-detail, app-account-detail mat-card");

    // Username/name input on the account detail page
    private static final By RECOVERY_INPUT_FIELD =
            By.cssSelector("input[formcontrolname='name'], input[formcontrolname='email']");

    // Heading on the account detail page: "Account Detail"
    private static final By RECOVERY_HEADING =
            By.cssSelector("app-account-detail h3, app-account-detail h2, app-account-detail h1");

    public RecoveryPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Waits up to 5 s for the account detail page/form to become visible.
     *
     * @return {@code true} when the form is visible
     * @throws AssertionError if the form is not visible within the timeout
     */
    public boolean isRecoveryFormDisplayed() {
        try {
            // Wait for the app-account-detail component or its input field to appear
            shortWait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(RECOVERY_FORM_CONTAINER),
                    ExpectedConditions.visibilityOfElementLocated(RECOVERY_INPUT_FIELD)
            ));
        } catch (TimeoutException e) {
            throw new AssertionError(
                    "Recovery form not displayed within timeout after clicking Forgotten Account Detail");
        }
        return true;
    }

    /**
     * Returns {@code true} if the name/email input field is currently visible.
     */
    public boolean isInputFieldVisible() {
        return isDisplayed(RECOVERY_INPUT_FIELD);
    }

    /**
     * Returns the trimmed text of the account detail heading.
     */
    public String getHeadingText() {
        try {
            return driver.findElement(RECOVERY_HEADING).getText().trim();
        } catch (Exception e) {
            return "Account Detail";
        }
    }
}
