package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Manages ChromeDriver lifecycle per thread.
 *
 * @author Maribel Aiza
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    private DriverManager() {
        // Utility class — no instantiation
    }

    /**
     * Initialises a ChromeDriver instance for the current thread.
     * WebDriverManager resolves the chromedriver binary automatically.
     * No implicit waits are set; all synchronisation uses explicit waits.
     */
    public static void initDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--disable-web-security");
        options.setAcceptInsecureCerts(true);

        WebDriver driver = new ChromeDriver(options);
        // No implicit waits — explicit waits only (WebDriverWait)
        driverThread.set(driver);
    }

    /**
     * Returns the WebDriver instance bound to the current thread.
     *
     * @return the current thread's WebDriver, or {@code null} if not initialised
     */
    public static WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Quits the WebDriver instance bound to the current thread and removes the
     * ThreadLocal entry. Any exception thrown by {@code driver.quit()} propagates
     * to the caller.
     */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}
