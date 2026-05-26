package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ReportManager;

/**
 * TC01 – Valid Login
 * Verifies that a user with valid credentials can log in and land on the dashboard.
 * Satisfies requirement R3.
 */
public class TC01_LoginValidTest extends BaseTest {

    @Test
    public void tc01_loginValid() {
        String username       = ConfigReader.get("login.username");
        String password       = ConfigReader.get("login.password");
        String dashboardTitle = ConfigReader.get("dashboard.title");

        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);

        // submitValidLogin() waits for the dashboard/property URL and throws AssertionError on timeout
        loginPage.submitValidLogin();

        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(
                currentUrl.matches(".*#/dashboard.*") || currentUrl.matches(".*#/property.*"),
                "Expected URL to contain #/dashboard or #/property but was: " + currentUrl);
        ReportManager.logPass("URL after valid login is correct: " + currentUrl);

        DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());

        Assert.assertTrue(
                dashboardPage.getPageTitle().contains(dashboardTitle),
                "Page title does not contain '" + dashboardTitle + "': " + dashboardPage.getPageTitle());
        ReportManager.logPass("Page title contains expected value: " + dashboardPage.getPageTitle());

        Assert.assertTrue(
                dashboardPage.isDashboardWelcomeVisible(),
                "Dashboard welcome element is not visible after valid login");
        ReportManager.logPass("Dashboard welcome element is visible");
    }
}
