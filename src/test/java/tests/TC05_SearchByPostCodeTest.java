package tests;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PropertySearchPage;
import utils.ConfigReader;
import utils.DriverManager;


/**
 * TC05 – Search by Post Code
 * Verifies that searching by post code returns at least one result with a non-empty address cell.
 * Satisfies requirement R7.
 *
 * @author Maribel Aiza
 */
public class TC05_SearchByPostCodeTest extends BaseTest {

    @Test
    public void tc05_searchByPostCode() {
        loginWithValidCredentials();

        String postCode = ConfigReader.get("search.postCode");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        searchPage.searchByPostCode(postCode);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for post code search");
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for post code search but found: " + searchPage.getResultCount());
        System.out.println("[PASS] " + "Post code search returned " + searchPage.getResultCount() + " result(s)");

        for (WebElement row : searchPage.getResultRows()) {
            String addressCell = searchPage.getAddressCellText(row);
            Assert.assertFalse(
                    addressCell.isEmpty(),
                    "Address cell text should not be empty in a result row");
        }
        System.out.println("[PASS] " + "All result rows have a non-empty address cell");
    }

    // -------------------------------------------------------------------------
    // Helper
    // -------------------------------------------------------------------------

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
