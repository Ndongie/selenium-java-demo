package pages;

import POJO.OrderDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class CheckOutOverviewPage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(CheckOutOverviewPage.class);
    private final By orderDetails = By.cssSelector("#checkout-overview .text-md");
    private final By finishButon = By.xpath("//div[@id='checkout-overview']/div[3]/button[2]");
    private final By orderConfirmationHeading = By.xpath("//div[@id='checkout-complete']/div/h3");

    public CheckOutOverviewPage(WebDriver driver){
        this.driver = driver;
    }

    public OrderDetails getOrderDetails(){
        logger.info("Getting the details of the order");
        // Get the paymentInformation
        String paymentInformation = driver.findElements(orderDetails).get(0).getText().trim();

        // Get the shipping information
        String shippingInformation = driver.findElements(orderDetails).get(1).getText().trim();

        // Get the itemTotalPrice, tax and totalPrice then create the orderDetails object
        OrderDetails orderDetails = new OrderDetails(
                paymentInformation,
                shippingInformation,
                getValue(2),
                getValue(3),
                getValue(4));
        logger.info("Orders details: {}", orderDetails.toString());
        return orderDetails;
    }

    public OrderConfirmationPage clickFinishButton(){
        logger.info("Clicking the finish button");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(10000));
        driver.findElement(finishButon).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(orderConfirmationHeading));
        logger.info("Finish button found and successfully clicked");
        return new OrderConfirmationPage(driver);
    }

    private Float getValue(int index){
        String rawPrice = driver.findElements(orderDetails).get(index)
                .getText()
                .trim()
                .split(":")[1]
                .replace("$", "");

        logger.info("Raw price {}", rawPrice);
        return Float.parseFloat(rawPrice);
    }
}
