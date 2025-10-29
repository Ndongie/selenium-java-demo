package base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry extends BaseTest implements IRetryAnalyzer {
    private final Logger logger = LoggerFactory.getLogger(ExtentReporter.class);
    int count = 0;
    int maxTry = maxRetry;

    @Override
    public boolean retry(ITestResult result) {

        logger.info("Rerunning the test.....");
        if(count < maxTry){
            count++;
            return true;
        }
        logger.info("Rerunning the test.....");
        return false;
    }
}
