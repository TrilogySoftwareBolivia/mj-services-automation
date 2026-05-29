package tests;

import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PropertySearchPage;
import utils.ConfigReader;
import utils.DriverManager;


/**
 * TC09 – Find a Property / Builder's After Care Id
 * Verifies that searching by Builder After Care ID returns at least one result.
 *
 * @author Maribel Aiza
 */
public class TC09_SearchByBuilderAfterCareIdTest extends BaseTest {

    @Test
    public void tc09_searchByBuilderAfterCareId() {
        loginWithValidCredentials();

        String id = ConfigReader.get("search.builderAfterCareId");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        System.out.println("[PASS] " + "Find a Property dialog opened");

        searchPage.searchByBuilderAfterCareId(id);
        System.out.println("[PASS] " + "Searched by Builder After Care Id: " + id);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for Builder After Care Id: " + id);
        }

        Assert.assertTrue(
                searchPage.getResultCount() >= 1,
                "Expected at least 1 result for Builder After Care Id search but found: " + searchPage.getResultCount());
        System.out.println("[PASS] " + "Builder After Care Id search returned " + searchPage.getResultCount() + " result(s)");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
