package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RecoveryPage;
import utils.DriverManager;


/**
 * TC03 – Forgotten Account
 * Verifies that clicking the "Forgotten" account link opens the account recovery form.
 * Satisfies requirement R5.
 *
 * @author Maribel Aiza
 */
public class TC03_ForgottenAccountTest extends BaseTest {

    @Test
    public void tc03_forgottenAccount() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());

        RecoveryPage recoveryPage = loginPage.clickForgottenAccountLink();

        // isRecoveryFormDisplayed() waits up to 5 s and throws AssertionError on timeout
        Assert.assertTrue(
                recoveryPage.isRecoveryFormDisplayed(),
                "Recovery form should be displayed after clicking Forgotten Account link");
        System.out.println("[PASS] " + "Recovery form is displayed after clicking Forgotten Account link");

        Assert.assertTrue(
                recoveryPage.isInputFieldVisible(),
                "Recovery input field should be visible");
        System.out.println("[PASS] " + "Recovery input field is visible");

        String headingText = recoveryPage.getHeadingText();
        Assert.assertFalse(
                headingText.isEmpty(),
                "Recovery form heading text should not be empty");
        System.out.println("[PASS] " + "Recovery form heading text is not empty: " + headingText);
    }
}
