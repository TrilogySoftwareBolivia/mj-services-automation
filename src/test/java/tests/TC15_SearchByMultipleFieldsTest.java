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
 * TC15 – Find a Property / Search by more than two fields
 * Verifies that searching by Name + Post Code + Fitting No. together returns at least one result.
 */
public class TC15_SearchByMultipleFieldsTest extends BaseTest {

    @Test
    public void tc15_searchByMultipleFields() {
        loginWithValidCredentials();

        String name      = ConfigReader.get("search.customerName");
        String postCode  = ConfigReader.get("search.postCode");
        String fittingNo = ConfigReader.get("search.fittingNo");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        ReportManager.logPass("Find a Property dialog opened");

        searchPage.searchByMultipleFields(name, postCode, fittingNo);
        ReportManager.logPass("Searched by Name: " + name
                + ", Post Code: " + postCode
                + ", Fitting No: " + fittingNo);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for multi-field search");
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for multi-field search but found: " + searchPage.getResultCount());
        ReportManager.logPass("Multi-field search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
