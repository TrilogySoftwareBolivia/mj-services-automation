package tests;

import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PropertySearchPage;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ReportManager;

/**
 * TC12 – Find a Property / Quote Number
 * Verifies that searching by quote number returns at least one result.
 */
public class TC12_SearchByQuoteIdTest extends BaseTest {

    @Test
    public void tc12_searchByQuoteId() {
        loginWithValidCredentials();

        String quoteId = ConfigReader.get("search.quoteId");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        ReportManager.logPass("Find a Property dialog opened");

        searchPage.searchByQuoteId(quoteId);
        ReportManager.logPass("Searched by Quote Number: " + quoteId);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for Quote Number: " + quoteId);
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for Quote Number search but found: " + searchPage.getResultCount());
        ReportManager.logPass("Quote Number search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
