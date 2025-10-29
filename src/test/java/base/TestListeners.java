package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

public class TestListeners extends BaseTest implements ITestListener{
    ExtentReports extent = ExtentReporter.getReportObject();
    ExtentTest test;
    WebDriver driver;
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static final Logger logger = LoggerFactory.getLogger(TestListeners.class);

    @Override
    public void onTestStart(ITestResult result) {
        try{
            logger.info("Running "+result.getMethod().getMethodName()+ " Test........");
            test = extent.createTest(result.getMethod().getMethodName());
            extentTest.set(test);//Unique thread id
            logger.info("Test successfully started");
        }
        catch (SkipException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+ " Test passed");
        extentTest.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+ " Test failed");
        extentTest.get().fail(result.getThrowable());

        Object currentClass = result.getInstance();
        driver = ((BaseTest) currentClass).getCurrentDriver();

        //Take screenshot and attach to report
        logger.info("Taking screenshot of failed section");
        String filePath = getScreenshot(result.getMethod().getMethodName(), driver);
        extentTest.get().addScreenCaptureFromPath(filePath, result.getMethod().getMethodName());
        logger.info("Screenshot of failed section taken and stored in: " +filePath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+ " Test skipped");
        extentTest.get().log(Status.SKIP, "Test Skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {

        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
