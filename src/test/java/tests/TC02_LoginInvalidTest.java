package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverManager;


/**
 * TC02 – Invalid Login
 * Verifies that a user with invalid credentials cannot log in and sees an error message.
 * Satisfies requirement R4.
 *
 * @author Maribel Aiza
 */
public class TC02_LoginInvalidTest extends BaseTest {

    @Test
    public void tc02_loginInvalid() {
        String invalidUsername = ConfigReader.get("login.invalid.username");
        String invalidPassword = ConfigReader.get("login.invalid.password");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(invalidUsername);
        loginPage.enterPassword(invalidPassword);

        // submitInvalidLogin() clicks the button without waiting for a URL change
        loginPage.submitInvalidLogin();

        String url = DriverManager.getDriver().getCurrentUrl();
        Assert.assertFalse(
                url.matches(".*#/dashboard.*"),
                "URL should NOT match #/dashboard after invalid login but was: " + url);
        System.out.println("[PASS] " + "URL does not contain #/dashboard after invalid login: " + url);

        // isErrorMessageDisplayed() waits up to 5 s and throws AssertionError on timeout
        Assert.assertTrue(
                loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed after invalid login");
        System.out.println("[PASS] " + "Error message is displayed after invalid login");

        String text = loginPage.getErrorMessageText();
        Assert.assertFalse(
                text.isEmpty(),
                "Error message text should not be empty");
        System.out.println("[PASS] " + "Error message text is not empty: " + text);
    }
}
