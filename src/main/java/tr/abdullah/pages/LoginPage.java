package tr.abdullah.pages;

import tr.abdullah.config.ConfigurationReader;

/**
 * LoginPage encapsulates actions and validations available on the SauceDemo Login view.
 */
public class LoginPage extends BasePage {

    // Target JSON repository file name under resources/elements/
    private static final String PAGE_NAME = "login_page";

    /**
     * Navigates to the base application URL configured in config.properties.
     */
    public void openLoginPage() {
        String baseUrl = ConfigurationReader.getProperty("base.url");
        navigateTo(baseUrl);
    }

    /**
     * Enters username into the username field.
     *
     * @param username Account username
     */
    public void enterUsername(String username) {
        sendKeys(PAGE_NAME, "username_input", username);
    }

    /**
     * Enters password into the password field.
     *
     * @param password Account password
     */
    public void enterPassword(String password) {
        sendKeys(PAGE_NAME, "password_input", password);
    }

    /**
     * Clicks the login submit button.
     */
    public void clickLoginButton() {
        click(PAGE_NAME, "login_button");
    }

    /**
     * Executes standard login flow in one step.
     *
     * @param username Account username
     * @param password Account password
     */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Retrieves the text from the login error banner.
     *
     * @return Displayed error message string
     */
    public String getErrorMessage() {
        return getText(PAGE_NAME, "error_message");
    }

    /**
     * Checks if the login error banner is displayed.
     *
     * @return True if visible, false otherwise
     */
    public boolean isErrorMessageDisplayed() {
        return isDisplayed(PAGE_NAME, "error_message");
    }
}