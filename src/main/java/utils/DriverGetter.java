package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DriverGetter {

    private static final Logger logger = LoggerFactory.getLogger(DriverGetter.class);

    public static WebDriver getDriver(String browser, String browser_mode, String device_name, String language, String headless){
        WebDriver driver;
        String lang="";

        // Set the language
        if(language.equalsIgnoreCase("en")){
            lang="en-US";
        }
        else{
            lang="fr-FR";
        }

        if(browser.equalsIgnoreCase("chrome")){
            logger.info("Setting up a ChromeDriver in {} mode", browser_mode);
            ChromeOptions chromeOptions = new ChromeOptions();

            // Set the headless mode
            if(headless.equalsIgnoreCase("true")){
                chromeOptions.addArguments("--headless=new");
                chromeOptions.addArguments("--disable-gpu");
            }

            if(browser_mode.equalsIgnoreCase("MOBILE")){
                logger.info("Opening chrome in mobile mode.....");
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", device_name);
                chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
                logger.info("Chrome successfully opened in mobile mode");
            }

            chromeOptions.addArguments("--lang="+lang);
            driver = new ChromeDriver(chromeOptions);
            logger.info("ChromeDriver successfully created");
        }
        else if(browser.equalsIgnoreCase("edge")){
            logger.info("Setting up an EdgeDriver");
            EdgeOptions edgeOptions = new EdgeOptions();

            // Set the headless mode
            if(headless.equalsIgnoreCase("true")){
                edgeOptions.addArguments("--headless");
                edgeOptions.addArguments("--disable-gpu");
            }

            driver = new EdgeDriver();
            logger.info("EdgeDriver successfully created");
        }
        else if(browser.equalsIgnoreCase("firefox")){
            logger.info("Setting up a FirefoxDriver");
            FirefoxOptions firefoxOptions = new FirefoxOptions();

            // Set the headless mode
            if(headless.equalsIgnoreCase("true")){
                firefoxOptions.addArguments("--headless");
            }

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
