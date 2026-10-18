package tr.abdullah.stepdefs;

import tr.abdullah.pages.ProductsPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions handling product selection and cart badge assertions.
 */
public class ProductsStepDefs {

    private final ProductsPage productsPage = new ProductsPage();

    @When("user adds product {string} to the cart")
    public void userAddsProductToTheCart(String productName) {
        productsPage.addProductToCart(productName);
    }

    @Then("the shopping cart badge should display {string}")
    public void theShoppingCartBadgeShouldDisplay(String expectedCount) {
        String actualCount = productsPage.getCartItemCount();
        Assert.assertEquals(actualCount, expectedCount, "Cart item badge count mismatch!");
    }

    @When("user navigates to the shopping cart")
    public void userNavigatesToTheShoppingCart() {
        productsPage.clickShoppingCart();
    }
}