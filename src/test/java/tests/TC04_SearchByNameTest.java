package tests;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PropertySearchPage;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ReportManager;

/**
 * TC04 – Search by Customer/Builder Name
 * Verifies that searching by customer name returns at least one result.
 * Satisfies requirement R6.
 */
public class TC04_SearchByNameTest extends BaseTest {

    @Test
    public void tc04_searchByName() {
        loginWithValidCredentials();

        String customerName = ConfigReader.get("search.customerName");

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        searchPage.searchByName(customerName);

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("No results returned within timeout for customer/builder name search");
        }

        int count = searchPage.getResultCount();
        Assert.assertTrue(
                count >= 1,
                "Expected at least 1 result for name search but found: " + count);
        ReportManager.logPass("Name search returned " + count + " result(s)");

        // Verify at least one result row has a non-empty name cell
        // (ag-Grid may render extra virtual/empty rows — we only require at least one populated row)
        boolean foundNonEmptyRow = false;
        for (WebElement row : searchPage.getResultRows()) {
            String nameCell = searchPage.getNameCellText(row);
            if (nameCell != null && !nameCell.isEmpty()) {
                foundNonEmptyRow = true;
                break;
            }
        }
        Assert.assertTrue(foundNonEmptyRow,
                "Expected at least one result row with a non-empty name cell");
        ReportManager.logPass("At least one result row has a non-empty name cell");
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
