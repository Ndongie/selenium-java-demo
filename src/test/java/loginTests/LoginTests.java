package loginTests;

import base.BaseTest;
import base.Retry;
import base.TestListeners;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

@Listeners(TestListeners.class)
public class LoginTests extends BaseTest {
    @Test(groups = {"regression","authentication", "smoke"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void loginWithInvalidEmail(){
        //Enter the email, password and click the login button
        loginPage.enterUserEmail(userData.get(0).get("email")+"invalid");
        loginPage.enterUserPassword(userData.get(0).get("password"));
        loginPage.clickLoginButton();

        //Get the error message
        String actualMessage = loginPage.getErrorMessage();

        assertTrue(
                actualMessage.equalsIgnoreCase(orderingData.get(0).get("loginWithInvalidEmailMessage")),
                "Expected:" +orderingData.get(0).get("loginWithInvalidEmailMessage")+ " but got" +actualMessage);
    }

    @Test(groups = {"regression","authentication", "smoke"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void loginWithInvalidPassword(){
        //Enter the email, password and click the login button
        loginPage.enterUserEmail(userData.get(0).get("email"));
        loginPage.enterUserPassword(userData.get(0).get("password")+"invalid");
        loginPage.clickLoginButton();

        //Get the error message
        String actualMessage = loginPage.getErrorMessage();

        assertTrue(
                actualMessage.equalsIgnoreCase(orderingData.get(0).get("loginWithInvalidPasswordMessage")),
                "Expected:" +orderingData.get(0).get("loginWithInvalidPasswordMessage")+ " but got" +actualMessage);
    }

    @Test(groups = {"regression","authentication", "smoke"}, retryAnalyzer = Retry.class, alwaysRun = true)
    void loginWithValidCredentials(){
        HomePage homePage = loginPage.login(userData.get(0).get("email"), userData.get(0).get("password"));

        assertNotNull(homePage, "The object is null. Login failed");
    }
}
