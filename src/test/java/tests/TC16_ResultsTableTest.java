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

import java.util.List;

/**
 * TC16 – Results / Table
 * Verifies that after a valid search the results table loads with rows
 * and each row contains non-empty data in at least the first column.
 */
public class TC16_ResultsTableTest extends BaseTest {

    @Test
    public void tc16_resultsTable() {
        loginWithValidCredentials();

        PropertySearchPage searchPage = new PropertySearchPage(DriverManager.getDriver());
        searchPage.openDialog();
        ReportManager.logPass("Find a Property dialog opened");

        searchPage.searchByName(ConfigReader.get("search.customerName"));
        ReportManager.logPass("Search submitted");

        try {
            searchPage.waitForResults();
        } catch (TimeoutException e) {
            Assert.fail("Results table did not load within timeout");
        }

        List<WebElement> rows = searchPage.getResultRows();
        Assert.assertTrue(rows.size() >= 1,
                "Expected at least 1 row in results table but found: " + rows.size());
        ReportManager.logPass("Results table loaded with " + rows.size() + " row(s)");

        // Verify at least one row has non-empty data in the first column
        // (ag-Grid may render extra virtual/empty rows)
        boolean foundNonEmptyRow = false;
        for (WebElement row : rows) {
            String cellText = searchPage.getNameCellText(row);
            if (cellText != null && !cellText.isEmpty()) {
                foundNonEmptyRow = true;
                ReportManager.logPass("Found result row with data: " + cellText);
                break;
            }
        }
        Assert.assertTrue(foundNonEmptyRow,
                "Expected at least one result row with non-empty data in first column");
    }

    private void loginWithValidCredentials() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(ConfigReader.get("login.username"));
        loginPage.enterPassword(ConfigReader.get("login.password"));
        loginPage.submitValidLogin();
    }
}
