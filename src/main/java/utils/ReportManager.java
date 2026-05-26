package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton utility that manages ExtentReports lifecycle.
 * Initialises a timestamped HTML report in target/reports/ and provides
 * thread-safe test logging via a ThreadLocal<ExtentTest>.
 */
public class ReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    // Prevent instantiation
    private ReportManager() {}

    /**
     * Creates the target/reports/ directory (if absent) and initialises a new
     * ExtentReports instance backed by an offline ExtentSparkReporter.
     * Must be called once per test suite (from ReportListener.onStart).
     */
    public static void initReport() {
        new File("target/reports").mkdirs();

        String reportPath = "target/reports/report_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setOfflineMode(true);

        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    /**
     * Creates a new test node in the report and binds it to the current thread.
     *
     * @param testName display name for the test in the report
     */
    public static void createTest(String testName) {
        testThread.set(extent.createTest(testName));
    }

    /**
     * Returns the ExtentTest bound to the current thread.
     *
     * @return the current thread's ExtentTest instance
     */
    public static ExtentTest getTest() {
        return testThread.get();
    }

    /**
     * Logs a PASS entry on the current thread's test node.
     *
     * @param message description of the passing step
     */
    public static void logPass(String message) {
        getTest().pass(message);
    }

    /**
     * Logs a FAIL entry on the current thread's test node.
     * Attempts to embed a Base64 screenshot; if capture fails the error is
     * printed to stderr and the failure is logged without a screenshot.
     *
     * @param message description of the failure
     * @param driver  WebDriver instance used to capture the screenshot
     */
    public static void logFail(String message, WebDriver driver) {
        try {
            String b64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
            getTest().fail(message,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(b64).build());
        } catch (Exception e) {
            System.err.println("Screenshot capture failed: " + e.getMessage());
            getTest().fail(message);
        }
    }

    /**
     * Flushes all pending report data to disk.
     * Must be called once per test suite (from ReportListener.onFinish).
     */
    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
