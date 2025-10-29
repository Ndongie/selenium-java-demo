package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderConfirmationPage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(OrderConfirmationPage.class);
    private final By orderConfirmationHeading = By.xpath("//div[@id='checkout-complete']/div/h3");
    private final By orderConfirmationBody = By.xpath("//div[@id='checkout-complete']/div/p");

    public OrderConfirmationPage(WebDriver driver){
        this.driver = driver;
    }

    public String getOrderConfirmationHeading(){
        logger.info("Getting the order confirmation heading");
        String text = driver.findElement(orderConfirmationHeading).getText().trim();
        logger.info("order confirmation heading found: {}", text);
        return text;
    }

    public String getOrderConfirmationBody(){
        logger.info("Getting the order confirmation body");
        String text = driver.findElement(orderConfirmationBody).getText().trim();
        logger.info("order confirmation body found: {}", text);
        return text;
    }
}
