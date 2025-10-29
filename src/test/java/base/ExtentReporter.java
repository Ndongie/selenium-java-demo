package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExtentReporter {

    private static final Logger logger = LoggerFactory.getLogger(ExtentReporter.class);

    public static ExtentReports getReportObject(){
        logger.info("Creating the report......");

        String path = System.getProperty("user.dir")+"\\src\\test\\Reports\\index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName(" SPORTIF TEST RESULTS");
        reporter.config().setDocumentTitle("Test Results");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Test Automation");

        logger.info("Report with file path:{}", path);

        return extent;
    }
}
