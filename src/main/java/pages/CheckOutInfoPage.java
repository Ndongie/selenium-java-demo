package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SeleniumActions;

public class CheckOutInfoPage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(CheckOutInfoPage.class);
    private final By continueButton = By.xpath("//div[@id='checkout-info']/div/button[2]");
    private final By userInputFields = By.cssSelector(".form-group input");

    public CheckOutInfoPage(WebDriver driver){
        this.driver = driver;
    }

    public void enterUserInformation(String firstName, String lastname, String zipCode){
        logger.info("Entering the user information....");
        //Enter the firstname
        driver.findElements(userInputFields).get(1).sendKeys(firstName);

        //Enter the lastname
        driver.findElements(userInputFields).get(2).sendKeys(lastname);

        //Enter the zip code
        driver.findElements(userInputFields).get(1).sendKeys(zipCode);

        logger.info("User information successfully entered");
    }

    public CheckOutOverviewPage clickContinueButton(){
        logger.info("Clicking the continue button");
        SeleniumActions.scrollToElementAndClick(driver, driver.findElement(continueButton));
        logger.info("Continue button successfully clicked");
        return new CheckOutOverviewPage(driver);
    }
}
