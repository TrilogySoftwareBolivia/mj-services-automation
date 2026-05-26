package tests;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ReportListener;
import utils.ReportManager;

import java.lang.reflect.Method;

/**
 * Base class for all TestNG test classes.
 *
 * Responsibilities:
 *  - Initialises a ChromeDriver instance before each test method (R2.4, R2.5)
 *  - Creates an ExtentReports test node named after the test method
 *  - Navigates to the configured base URL
 *  - On failure, logs a screenshot-embedded fail entry in the report (R10)
 *  - Always quits the driver after each test method; exceptions propagate
 */
@Listeners(ReportListener.class)
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
        ReportManager.createTest(method.getName());
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
            ReportManager.logFail(result.getThrowable().getMessage(), DriverManager.getDriver());
        }
        DriverManager.quitDriver();
    }
}
