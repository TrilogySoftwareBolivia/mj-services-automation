package tests;

import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PropertySearchPage;
import utils.ConfigReader;
import utils.DriverManager;


/**
 * TC08 – Find a Property / Service No.
 * Verifies that searching by service number returns at least one result.
 *
 * @author Maribel Aiza
 */
public class TC08_SearchByServiceNoTest extends BaseTest {

    @Test
    public void tc08_searchByServiceNo() {
        loginWithValidCredentials();

        String serviceNo = ConfigReader.get("search.serviceNo");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        System.out.println("[PASS] " + "Find a Property dialog opened");

        searchPage.searchByServiceNo(serviceNo);
        System.out.println("[PASS] " + "Searched by Service No: " + serviceNo);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for Service No: " + serviceNo);
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for Service No search but found: " + searchPage.getResultCount());
        System.out.println("[PASS] " + "Service No search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
