package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.apache.commons.io.FileUtils.readFileToString;

public class Utilities {
    private static final Logger logger = LoggerFactory.getLogger(Utilities.class);

    public static int generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive");
        }

        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static List<HashMap<String, String>> getJsonDataToMap(String filePath){
        logger.info("Generating data from json file: {}", filePath);

        try {
            //read json to string
            String jsonContent = readFileToString(new File(filePath), StandardCharsets.UTF_8);

            //String to jackson databind
            ObjectMapper mapper = new ObjectMapper();
            logger.info("Data successfully generated from json file");

            return mapper.readValue(jsonContent, new TypeReference<>() {
            });

        } catch (Exception e) {
            logger.info("Data could not be generated from json file");
            e.printStackTrace();
        }

        return null;
    }

    public static int getElementFromList(WebDriver driver, By locator, String name){
        logger.info("Getting element {} in list of elements located by {}", name, locator);
        //Get the index of the element you want to add to cart
        List<WebElement> elements = driver.findElements(locator);
        int index = 0;

        for(int i=0; i < elements.size(); i++){
            // Get the text from the element
            String text = elements.get(i).getText().trim();

            logger.info("Checking if {} = {}", text, name);
            if(text.equalsIgnoreCase(name)){
                index = i;
                logger.info("{} = {}, element found at index {}", text, name, index);
                break;
            }
            else{
                logger.info("Element not found");
            }
        }

        return index;
    }
}
