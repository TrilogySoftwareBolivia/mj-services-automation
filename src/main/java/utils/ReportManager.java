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
 * thread-safe test logging via a ThreadLocal ExtentTest.
 *
 * @author Maribel Aiza
 */
public class ReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ReportManager() {}

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

    public static void createTest(String testName) {
        testThread.set(extent.createTest(testName));
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void logPass(String message) {
        ExtentTest test = getTest();
        if (test != null) {
            test.pass(message);
        }
        System.out.println("[PASS] " + message);
    }

    public static void logFail(String message, WebDriver driver) {
        ExtentTest test = getTest();
        if (test != null) {
            try {
                String b64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                test.fail(message,
                        MediaEntityBuilder.createScreenCaptureFromBase64String(b64).build());
            } catch (Exception e) {
                test.fail(message);
            }
        }
        System.out.println("[FAIL] " + message);
    }

    public static void flushReport() {
        if (extent != null) {
            extent.flush();
        }
    }
}
