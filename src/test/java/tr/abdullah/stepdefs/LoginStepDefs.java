package tr.abdullah.stepdefs;

import tr.abdullah.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions implementing Gherkin actions and assertions for user authentication.
 */
public class LoginStepDefs {

    private final LoginPage loginPage = new LoginPage();

    @Given("user navigates to the login page")
    public void userNavigatesToTheLoginPage() {
        loginPage.openLoginPage();
    }

    @When("user enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String username, String password) {
        if (!username.isEmpty()) {
            loginPage.enterUsername(username);
        }

        if (!password.isEmpty()) {
            loginPage.enterPassword(password);
        }
    }

    @And("user clicks on the login button")
    public void userClicksOnTheLoginButton() {
        loginPage.clickLoginButton();
    }

    @Then("user should be redirected to the inventory catalog page")
    public void userShouldBeRedirectedToTheInventoryCatalogPage() {
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("inventory.html"),
                "User was not redirected to inventory page! Current URL: " + currentUrl);
    }

    @And("the page URL should contain {string}")
    public void thePageURLShouldContain(String expectedUrlPart) {
        String currentUrl = loginPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains(expectedUrlPart),
                String.format("Expected URL to contain '%s', but was '%s'", expectedUrlPart, currentUrl));
    }

    @Then("an error message stating {string} should be displayed")
    public void anErrorMessageStatingShouldBeDisplayed(String expectedError) {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message element is not displayed!");
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, "Displayed error message does not match expected text!");
    }
}