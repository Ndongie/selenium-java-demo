package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SeleniumActions;
import utils.Utilities;

public class CartPage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);
    private final By checkoutButton = By.xpath("//div[@id='cart']/div/button[2]");
    private final By productNameLocator = By.cssSelector("#cart .mt-3 h3");

    public CartPage(WebDriver driver){
        this.driver = driver;
    }

    public Boolean checkItem(String productName){
        logger.info("Checking if {} exist in the cart", productName);
        int index = Utilities.getElementFromList(driver, productNameLocator, productName);
        String name = driver.findElements(productNameLocator).get(index).getText().trim();

        if(name.equalsIgnoreCase(productName)){
            logger.info("Product: {} found in cart", name);
            return  true;
        }
        else{
            logger.info("Product: {} was not found in cart ", name);
            return false;
        }
    }

    public CheckOutInfoPage clickCheckOutButton(){
        logger.info("Click the checkout button");
        SeleniumActions.scrollToElementAndClick(driver, driver.findElement(checkoutButton));
        logger.info("Checkout button successfully clicked");
        return new CheckOutInfoPage(driver);
    }
}
