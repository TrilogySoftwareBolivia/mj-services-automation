package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;

/**
 * TestNG listener that hooks into suite-level events to manage the
 * ExtentReports lifecycle via ReportManager.
 *
 * @author Maribel Aiza
 */
public class ReportListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        ReportManager.initReport();
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportManager.flushReport();
    }
}
