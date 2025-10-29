package base;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import utils.DriverGetter;
import utils.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import static org.openqa.selenium.OutputType.FILE;

public class BaseTest {
    private static String url;
    protected static String client;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    protected LoginPage loginPage;
    protected List<HashMap<String, String>> userData;
    protected List<HashMap<String, String>> productData;
    protected List<HashMap<String, String>> orderingData;

    protected String language;
    protected String browser_mode;
    private String browser;
    private String device_name;
    protected int maxRetry;
    private WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp(ITestContext result){
        String log4jConfPath = System.getProperty("user.dir") +"\\src\\main\\resources\\application.properties";
        PropertyConfigurator.configure(log4jConfPath);

        logger.info("Setting up the appropriate driver, browser and browser mode........");

        Properties properties = new Properties();
        try{
            FileInputStream fis = new FileInputStream(System.getProperty("user.dir") +"\\src\\main\\resources\\application.properties");
            properties.load(fis);
        }catch(Exception e){
            e.printStackTrace();
        }

        // Set the language
        language = System.getProperty("language");
        logger.info("User language: {}", language);

        //Set the default url, browser, browser mode or device name values when they are not null
        url = System.getProperty("url");
        client = System.getProperty("client");
        browser = System.getProperty("browser");
        browser_mode = System.getProperty("browser_mode");
        device_name = System.getProperty("device_name");
        String maxRetryString = System.getProperty("maxRetry");

        if(url == null){
            url = properties.getProperty("url");
            logger.info("No url sent. using the default url:{}", url);
        }
        if(language == null){
            language = properties.getProperty("language");
            logger.info("No language sent. using the default language:{}", language);
        }
        if(browser == null){
            browser = properties.getProperty("browser");
            logger.info("No browser sent. using the default browser:{}", browser);
        }
        if(browser_mode == null){
            browser_mode = properties.getProperty("browser_mode");
            logger.info("No browser mode sent. using the default browser mode:{}", browser_mode);
        }
        if(device_name == null){
            device_name = properties.getProperty("device_name");
            logger.info("No device sent. using the default device:{}", device_name);
        }
        if(maxRetryString == null){
            maxRetry = Integer.parseInt(properties.getProperty("maxRetry"));
            logger.info("No max retry sent. using the default value:{}", maxRetry);
        }else{
            maxRetry = Integer.parseInt(maxRetryString);
        }

        // Get the user data
        userData = Utilities.getJsonDataToMap(System.getProperty("user.dir") +
                "\\src\\test\\data\\testAccounts.json");
        productData  = Utilities.getJsonDataToMap(System.getProperty("user.dir") +
                "\\src\\test\\data\\products.json");

        orderingData = Utilities.getJsonDataToMap(System.getProperty("user.dir") +
                "\\src\\test\\data\\orderData.json");

        driver = DriverGetter.getDriver(browser, browser_mode, device_name, language);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(20000));
        driver.manage().window().maximize();

        logger.info("{} Successfully initialized", driver);

    }

    @BeforeMethod(alwaysRun = true)
    public void goHome() {
        logger.info("Launching the URL: {}", url);
        driver.get(url);
        logger.info("Url successfully launched");
        loginPage = new LoginPage(driver);
    }

    @AfterClass(alwaysRun = true)
    public void afterAll() {
        logger.info("Test(s) completed. Closing the browser........");
        driver.quit();
        logger.warn("Driver objected terminated. Browser will be closed");
    }

    public String getScreenshot(String testCaseName, WebDriver driver){
        logger.info("Test taking a screenshot........");
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File source = screenshot.getScreenshotAs(FILE);
        File file = new File(System.getProperty("user.dir") +"\\src\\test\\screenshots\\" +testCaseName+ ".png");
        try {
            FileUtils.copyFile(source, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logger.info("Screenshot taken and stored in path: {}.png",
                System.getProperty("user.dir")
                +"\\src\\test\\screenshots\\"
                +testCaseName);

        return System.getProperty("user.dir") +"\\src\\test\\screenshots\\" +testCaseName+ ".png";
    }

    public WebDriver getCurrentDriver(){
        return driver;
    }

}
