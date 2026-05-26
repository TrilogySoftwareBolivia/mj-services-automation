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
 * TC11 – Find a Property / Email Of Contact
 * Verifies that searching by email returns at least one result.
 */
public class TC11_SearchByEmailTest extends BaseTest {

    @Test
    public void tc11_searchByEmail() {
        loginWithValidCredentials();

        String email = ConfigReader.get("search.email");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        ReportManager.logPass("Find a Property dialog opened");

        searchPage.searchByEmail(email);
        ReportManager.logPass("Searched by Email: " + email);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for Email: " + email);
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for Email search but found: " + searchPage.getResultCount());
        ReportManager.logPass("Email search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
