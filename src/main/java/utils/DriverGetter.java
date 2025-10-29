package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DriverGetter {

    private static final Logger logger = LoggerFactory.getLogger(DriverGetter.class);

    public static WebDriver getDriver(String browser, String browser_mode, String device_name, String language){
        WebDriver driver;
        String lang="";

        if(language.equalsIgnoreCase("en")){
            lang="en-US";
        }
        else{
            lang="fr-FR";
        }

        if(browser.equalsIgnoreCase("chrome")){
            logger.info("Setting up a ChromeDriver in {} mode", browser_mode);
            //WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            if(browser_mode.equalsIgnoreCase("MOBILE")){
                logger.info("Opening chrome in mobile mode.....");
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", device_name);
                options.setExperimentalOption("mobileEmulation", mobileEmulation);
                logger.info("Chrome successfully opened in mobile mode");
            }

            options.addArguments("--lang="+lang);
            driver = new ChromeDriver(options);
            logger.info("ChromeDriver successfully created");
        }
        else if(browser.equalsIgnoreCase("edge")){
            logger.info("Setting up an EdgeDriver");
            driver = new EdgeDriver();
            logger.info("EdgeDriver successfully created");
        }
        else if(browser.equalsIgnoreCase("firefox")){
            logger.info("Setting up a FirefoxDriver");
            driver = new FirefoxDriver();
            logger.info("FirefoxDriver successfully created");
        }

        else if(browser.equalsIgnoreCase("safari")){
            logger.info("Setting up a SafariDriver");
            driver = new SafariDriver();
            logger.info("SafariDriver successfully created");
        }
        else {
            logger.info("The browser is not recognized. the supported browsers are chrome, edge, firefox and safari");
            driver = null;
        }

        return driver;
    }

}
