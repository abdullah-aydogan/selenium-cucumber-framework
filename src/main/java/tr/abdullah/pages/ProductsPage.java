package tr.abdullah.pages;

/**
 * ProductsPage encapsulates interactions on the catalog inventory page.
 */
public class ProductsPage extends BasePage {

    private static final String PAGE_NAME = "products_page";

    public String getPageHeaderTitle() {
        return getText(PAGE_NAME, "page_title");
    }

    public void addProductToCart(String productName) {
        click(PAGE_NAME, "add_to_cart_by_product_name", productName);
    }

    public String getCartItemCount() {
        if (isDisplayed(PAGE_NAME, "shopping_cart_badge")) {
            return getText(PAGE_NAME, "shopping_cart_badge");
        }
        return "0";
    }

    public void clickShoppingCart() {
        click(PAGE_NAME, "shopping_cart_link");
    }
}