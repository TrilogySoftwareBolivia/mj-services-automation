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
 * TC10 – Find a Property / Phone Number Of Contact
 * Verifies that searching by phone number returns at least one result.
 */
public class TC10_SearchByPhoneTest extends BaseTest {

    @Test
    public void tc10_searchByPhone() {
        loginWithValidCredentials();

        String phone = ConfigReader.get("search.phone");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        ReportManager.logPass("Find a Property dialog opened");

        searchPage.searchByPhone(phone);
        ReportManager.logPass("Searched by Phone Number: " + phone);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for Phone Number: " + phone);
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for Phone search but found: " + searchPage.getResultCount());
        ReportManager.logPass("Phone search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
