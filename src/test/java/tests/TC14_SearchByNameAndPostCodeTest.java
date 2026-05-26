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
 * TC14 – Find a Property / Customer/Builder Name + Address/Post Code
 * Verifies that searching by name AND post code together returns at least one result.
 */
public class TC14_SearchByNameAndPostCodeTest extends BaseTest {

    @Test
    public void tc14_searchByNameAndPostCode() {
        loginWithValidCredentials();

        String name     = ConfigReader.get("search.customerName");
        String postCode = ConfigReader.get("search.postCode");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        ReportManager.logPass("Find a Property dialog opened");

        searchPage.searchByNameAndPostCode(name, postCode);
        ReportManager.logPass("Searched by Name: " + name + " and Post Code: " + postCode);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for Name + Post Code search");
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for Name + Post Code search but found: " + searchPage.getResultCount());
        ReportManager.logPass("Name + Post Code search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
