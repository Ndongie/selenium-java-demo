package pages;

import POJO.Product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SeleniumActions;
import utils.Utilities;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HomePage {
    private final WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);
    private final By orderByField = By.cssSelector("button[data-slot='popover-trigger']");
    private final By orderByOptions = By.cssSelector("div[data-slot='command-item']");
    private final By productsLocator = By.cssSelector(".group");
    private final By productNameLocator = By.cssSelector("a"); // Second item
    private final By productNamesLocator = By.xpath("//div[contains(@class,'group')]/a[2]");
    private final By productPriceLocator = By.cssSelector("span"); // Second item
    private final By productPricesLocator = By.xpath("//div[contains(@class,'group')]/div/span");
    private final By addToCartButton = By.cssSelector("button");
    private final By addToCartButtonsLocator = By.xpath("//div[contains(@class,'group')]/div/button");
    private final By addToCartNotification = By.cssSelector("section ol");
    private final By gotoCartButton = By.cssSelector("#ecommerce-header span[role='button']");
    private final By cartProductNameLocator = By.cssSelector("#cart .mt-3 h3");
    private final WebDriverWait wait;

    public HomePage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(10000));
    }

    public void OrderItems(OrderingOptions orderingOptions){
        logger.info("Ordering items in {}", orderingOptions.name());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(orderByField).click(); // Click the order field
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(orderByOptions));

        switch (orderingOptions.name()){
            case "ASCENDING":
                driver.findElements(orderByOptions).get(0).click();
                break;
            case "DESCENDING":
                driver.findElements(orderByOptions).get(1).click();
                break;

            case "LOW_TO_HIGH_PRICE":
                driver.findElements(orderByOptions).get(2).click();
                break;
            case "HIGH_TO_LOW_PRICE":
                driver.findElements(orderByOptions).get(3).click();
                break;
        }
        //Wait for the elements to be ordered
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        logger.info("Items successfully ordered in {}", orderingOptions.name());
    }

    public List<Product> getProductList(){
        logger.info("Getting the list of products");
        List<Product> products = new ArrayList<>();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productsLocator));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driver.findElements(productsLocator).forEach(e -> {
            // Get the product name
            String productName = e.findElements(productNameLocator).get(1).getText().trim();

            //Get the product price
            String rawPrice = e.findElements(productPriceLocator).get(1).getText().trim().split("\\$")[1];
            Float productPrice = Float.parseFloat(rawPrice);

            products.add(new Product(productName, productPrice));
        });

        logger.info("Products found: {}", products);

        return products;
    }

    public void addToCart(String productName){
        logger.info("Adding {} to cart", productName);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productsLocator));

        // Add the item to cart
        int index = Utilities.getElementFromList(driver, productNamesLocator, productName);

        //Ensure the element is not yet added to cart
        logger.info("Checking the item is already added to the cart");
        String buttonText = driver.findElements(addToCartButtonsLocator)
                .get(index)
                .getText()
                        .trim();
        if(buttonText.equalsIgnoreCase("Remove from cart")){
            logger.info("The item is already added to cart");
            return;
        }

        logger.info("Item is not yet added to cart");
        SeleniumActions.scrollToElementAndClick(
                driver,
                driver.findElements(addToCartButtonsLocator).get(index)
        );

        wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartNotification));

        logger.info("{} successfully added to cart", productName);
    }

    public CartPage clickCartButton(){
        logger.info("Click the cart button");
        SeleniumActions.scrollToElementAndClick(driver, driver.findElement(gotoCartButton));
        wait.until(ExpectedConditions.presenceOfElementLocated(cartProductNameLocator));
        logger.info("cart button found and successfully clicked");
        return new CartPage(driver);
    }

}
