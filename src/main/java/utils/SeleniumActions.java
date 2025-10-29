package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class SeleniumActions {

    public static void scrollToElementAndClick(WebDriver driver, WebElement element){

        new Actions(driver)
                .moveToElement(element)
                .click()
                .perform();
    }

    public static void scroll(WebDriver driver, WebElement element){

        new Actions(driver)
                .moveToElement(element)
                .perform();
    }

}
