package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;

/**
 * TestNG listener that hooks into suite-level events to manage the
 * ExtentReports lifecycle via ReportManager.
 *
 * Register on BaseTest with: @Listeners(ReportListener.class)
 */
public class ReportListener implements ITestListener {

    /**
     * Called once before any test in the suite runs.
     * Initialises the ExtentReports instance and the output HTML file.
     */
    @Override
    public void onStart(ITestContext context) {
        ReportManager.initReport();
    }

    /**
     * Called once after all tests in the suite have finished.
     * Flushes all buffered report data to disk.
     */
    @Override
    public void onFinish(ITestContext context) {
        ReportManager.flushReport();
    }
}
