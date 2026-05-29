package tests;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;
import utils.DriverManager;

import java.lang.reflect.Method;

/**
 * Base class for all TestNG test classes.
 *
 * Responsibilities:
 *  - Initialises a ChromeDriver instance before each test method (R2.4, R2.5)
 *  - Navigates to the configured base URL
 *  - On failure, logs a fail entry to stdout (R10)
 *  - Always quits the driver after each test method; exceptions propagate
 *
 * @author Maribel Aiza
 */
public class BaseTest {

    /**
     * Runs before every test method.
     * Initialises the WebDriver, creates the report node, and opens the base URL.
     *
     * @param method the test method about to run (used as the report test name)
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        DriverManager.initDriver();
        System.out.println("[TEST START] " + method.getName());
        DriverManager.getDriver().get(ConfigReader.get("base.url"));
    }

    /**
     * Runs after every test method.
     * Logs a failure with screenshot if the test failed, then quits the driver.
     * Any exception thrown by {@code quitDriver()} propagates to TestNG.
     *
     * @param result the result of the test method that just ran
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            System.out.println("[TEST FAIL] " + result.getThrowable().getMessage());
        }
        DriverManager.quitDriver();
    }
}
