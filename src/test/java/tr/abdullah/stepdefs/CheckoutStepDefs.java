package tr.abdullah.stepdefs;

import tr.abdullah.pages.CartPage;
import tr.abdullah.pages.CheckoutPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions handling cart verification, checkout details, and order confirmations.
 */
public class CheckoutStepDefs {

    private final CartPage cartPage = new CartPage();
    private final CheckoutPage checkoutPage = new CheckoutPage();

    @Then("product {string} should be visible in the cart")
    public void productShouldBeVisibleInTheCart(String productName) {
        Assert.assertTrue(cartPage.isProductDisplayedInCart(productName),
                "Product '" + productName + "' was not found in the cart!");
    }

    @When("user proceeds to checkout")
    public void userProceedsToCheckout() {
        cartPage.clickCheckoutButton();
    }

    @And("user fills customer details with first name {string}, last name {string}, and postal code {string}")
    public void userFillsCustomerDetails(String firstName, String lastName, String postalCode) {
        checkoutPage.fillCustomerInformation(firstName, lastName, postalCode);
    }

    @And("user clicks continue on checkout overview")
    public void userClicksContinueOnCheckoutOverview() {
        checkoutPage.clickContinue();
    }

    @And("user completes the order by clicking finish")
    public void userCompletesTheOrderByClickingFinish() {
        checkoutPage.clickFinish();
    }

    @Then("the order confirmation message should be {string}")
    public void theOrderConfirmationMessageShouldBe(String expectedMessage) {
        String actualHeader = checkoutPage.getConfirmationHeader();
        Assert.assertEquals(actualHeader, expectedMessage, "Order confirmation message mismatch!");
    }

    @Then("a checkout error stating {string} should be displayed")
    public void aCheckoutErrorStatingShouldBeDisplayed(String expectedError) {
        Assert.assertTrue(checkoutPage.isErrorMessageDisplayed(), "Checkout error message is not displayed!");
        String actualError = checkoutPage.getErrorMessage();
        Assert.assertEquals(actualError, expectedError, "Checkout error message mismatch!");
    }

    @And("user fills customer details with dynamically generated fake data")
    public void userFillsCustomerDetailsWithDynamicallyGeneratedFakeData() {
        String firstName = tr.abdullah.utils.DataGenerator.getFirstName();
        String lastName =  tr.abdullah.utils.DataGenerator.getLastName();
        String postalCode =  tr.abdullah.utils.DataGenerator.getPostalCode();
        checkoutPage.fillCustomerInformation(firstName, lastName, postalCode);
    }
}