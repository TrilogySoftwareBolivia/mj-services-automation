package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Abstract base class for all Page Objects.
 * Provides shared WebDriver, explicit waits, and common helper methods.
 * Satisfies requirement R1.5.
 */
public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;       // 10 seconds
    protected WebDriverWait shortWait;  // 5 seconds

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    /**
     * Waits until the element located by {@code locator} is visible, using the
     * supplied {@link WebDriverWait} instance.
     *
     * @param locator the By locator for the target element
     * @param w       the WebDriverWait to use (e.g. {@code wait} or {@code shortWait})
     * @return the visible {@link WebElement}
     */
    protected WebElement waitForVisible(By locator, WebDriverWait w) {
        return w.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Returns {@code true} if the element located by {@code locator} is currently
     * displayed in the DOM, {@code false} if it is absent or stale.
     *
     * @param locator the By locator for the target element
     * @return whether the element is displayed
     */
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }
}
