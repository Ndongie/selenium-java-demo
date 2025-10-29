package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SeleniumActions;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);
    private final By emailField = By.id("email");
    private final By passwordField = By.id("password");
    private final By loginButton = By.xpath("//button[contains(@class, \"btn-submit\")]");
    private final By userEmailOnHomePage = By.cssSelector(".user-name");
    private static final By errorMessageField = By.cssSelector(".form-group p");

    public LoginPage(WebDriver driver){
        this.driver = driver;
    }

    public void enterUserEmail(String email){
        logger.info("Entering the email:{}", email);

        try {
            driver.findElement(emailField).sendKeys(email);
            logger.info("Email successfully entered");
        }
        catch(NoSuchElementException e){
            logger.info("Email field not found. Could not enter the email");
        }
    }

    public void enterUserPassword(String password){
        logger.info("Entering the password...");
        try {
            driver.findElement(passwordField).sendKeys(password);
            logger.info("Password successfully entered");
        }
        catch(NoSuchElementException e){
            logger.info("Password field not found. Could not enter the password");
        }
    }

    public void clickLoginButton(){
        logger.info("Clicking the login button...");

        try {
            WebElement element = driver.findElement(loginButton);
            SeleniumActions.scrollToElementAndClick(driver, element);
            logger.info("Login button successfully clicked");
        }
        catch(NoSuchElementException e){
            logger.info("Login button not found");
        }
    }

    public HomePage login(String email, String password){
        logger.info("Authentication the user....");
        enterUserEmail(email);
        enterUserPassword(password);

        // Click the login button
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        clickLoginButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(userEmailOnHomePage));
        logger.info("Login successful");

        return new HomePage(driver);
    }

    public String getErrorMessage(){
        logger.info("Getting the login error message");
        String text = driver.findElement(errorMessageField).getText().trim();
        logger.info("Error message found {}", text);
        return text;
    }
}
