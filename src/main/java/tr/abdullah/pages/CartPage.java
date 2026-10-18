package tr.abdullah.pages;

/**
 * CartPage encapsulates verification and actions inside the user's shopping basket.
 */
public class CartPage extends BasePage {

    private static final String PAGE_NAME = "cart_page";

    public boolean isProductDisplayedInCart(String productName) {
        return isDisplayed(PAGE_NAME, "cart_item_by_name", productName);
    }

    public void clickCheckoutButton() {
        click(PAGE_NAME, "checkout_button");
    }
}