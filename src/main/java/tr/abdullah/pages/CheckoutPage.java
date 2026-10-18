package tr.abdullah.pages;

/**
 * CheckoutPage encapsulates information entry, order review, and confirmation checks.
 */
public class CheckoutPage extends BasePage {

    private static final String PAGE_NAME = "checkout_page";

    public void fillCustomerInformation(String firstName, String lastName, String postalCode) {
        if (!firstName.isEmpty()) {
            sendKeys(PAGE_NAME, "first_name_input", firstName);
        }

        if (!lastName.isEmpty()) {
            sendKeys(PAGE_NAME, "last_name_input", lastName);
        }

        if (!postalCode.isEmpty()) {
            sendKeys(PAGE_NAME, "postal_code_input", postalCode);
        }
    }

    public void clickContinue() {
        click(PAGE_NAME, "continue_button");
    }

    public void clickFinish() {
        click(PAGE_NAME, "finish_button");
    }

    public String getConfirmationHeader() {
        return getText(PAGE_NAME, "complete_header");
    }

    public String getErrorMessage() {
        return getText(PAGE_NAME, "error_message");
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(PAGE_NAME, "error_message");
    }
}