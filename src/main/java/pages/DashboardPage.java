package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the Dashboard screen.
 * Satisfies requirement R3.
 *
 * @author Maribel Aiza
 */
public class DashboardPage extends BasePage {

    // The header toolbar is always present after login
    private static final By DASHBOARD_WELCOME =
            By.cssSelector("app-header mat-toolbar.mat-primary");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns {@code true} if the current URL indicates the dashboard or property page.
     */
    public boolean isLoaded() {
        String url = driver.getCurrentUrl();
        return url.matches(".*#/dashboard.*") || url.matches(".*#/property.*");
    }

    /**
     * Returns the browser page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns {@code true} if the dashboard welcome/header element is visible.
     */
    public boolean isDashboardWelcomeVisible() {
        return isDisplayed(DASHBOARD_WELCOME);
    }
}
