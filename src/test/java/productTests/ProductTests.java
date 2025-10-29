package productTests;

import POJO.Product;
import base.BaseTest;
import base.Retry;
import base.TestListeners;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.*;

import java.util.Comparator;
import java.util.List;

import static org.testng.Assert.*;

@Listeners(TestListeners.class)
public class ProductTests extends BaseTest {
    private HomePage homePage;

    @Test(priority = 1, groups = {"product", "regression"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void getItems(){
        login();
        assertFalse(homePage.getProductList().isEmpty(), "There are no products");
    }

    @Test(priority = 2, groups = {"product", "regression", "sorting"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void orderInAscendingOrder(){
       login();
        List<String> expecteProductList = homePage.getProductList().stream()
                .distinct()
                .map(Product::getName)
                .toList();

        homePage.OrderItems(OrderingOptions.ASCENDING);
        List<String> actualProductList = homePage.getProductList().stream()
                .distinct()
                .map(Product::getName)
                .toList();

        assertEquals(expecteProductList, actualProductList, "Products are not ordered in ascending order");
    }

    @Test(priority = 3, groups = {"product", "regression", "sorting"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void orderInDescendingOrder(){
        login();
        List<String> expecteProductList = homePage.getProductList().stream()
                .distinct()
                .sorted(Comparator.comparing(Product::getName).reversed())
                .map(Product::getName)
                .toList();

        homePage.OrderItems(OrderingOptions.DESCENDING);
        List<String> actualProductList = homePage.getProductList().stream()
                .distinct()
                .map(Product::getName)
                .toList();

        assertEquals(expecteProductList, actualProductList, "Products are not ordered in descending order\nExpected:" +expecteProductList+ "\nActual:" +actualProductList);
    }

    @Test(priority = 4, groups = {"product", "regression", "sorting"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void orderFromLowToHighPrice(){
        login();

        List<Float> expecteProductList = homePage.getProductList().stream()
                .distinct()
                .sorted(Comparator.comparing(Product::getPrice))
                .map(Product::getPrice)
                .toList();

        homePage.OrderItems(OrderingOptions.LOW_TO_HIGH_PRICE);
        List<Float> actualProductList = homePage.getProductList().stream()
                .distinct()
                .map(Product::getPrice)
                .toList();

        assertEquals(expecteProductList, actualProductList, "Products are not ordered from low to high price");
    }

    @Test(priority = 5, groups = {"product", "regression", "sorting"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void orderFromHighToLowPrice(){
        login();

        List<Float> expecteProductList = homePage.getProductList().stream()
                .distinct()
                .sorted(Comparator.comparing(Product::getPrice).reversed())
                .map(Product::getPrice)
                .toList();

        homePage.OrderItems(OrderingOptions.HIGH_TO_LOW_PRICE);
        List<Float> actualProductList = homePage.getProductList().stream()
                .distinct()
                .map(Product::getPrice)
                .toList();

        assertEquals(expecteProductList, actualProductList, "Products are not ordered from high to low price");
    }

    @Test(priority = 6, groups = {"product", "regression"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void AddToCart(){
        login();
        String productName = productData.get(0).get("name");
        homePage.addToCart(productName);
        CartPage cartPage = homePage.clickCartButton();
        assertTrue(cartPage.checkItem(productName), "Product was not found");
    }

    @Test(priority = 7, groups = {"product", "regression"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void orderItem(){
        login();
        String productName = productData.get(0).get("name");
        homePage.addToCart(productName);
        CartPage cartPage = homePage.clickCartButton();
        CheckOutInfoPage checkOutInfoPage = cartPage.clickCheckOutButton();
        CheckOutOverviewPage checkOutOverviewPage = checkOutInfoPage.clickContinueButton();
        checkOutOverviewPage.getOrderDetails();
        OrderConfirmationPage orderConfirmationPage = checkOutOverviewPage.clickFinishButton();
        String actualText = orderConfirmationPage.getOrderConfirmationHeading();
        assertTrue(actualText.equalsIgnoreCase(orderingData.get(0).get("heading")),
                "Order failed, " +actualText+ " is not equal to " +productData.get(0).get("heading"));
    }

    private void login(){
        homePage = loginPage.login(userData.get(0).get("email"), userData.get(0).get("password"));
    }
}
