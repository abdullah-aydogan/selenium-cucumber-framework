package tr.abdullah.pages;

import tr.abdullah.config.ConfigurationReader;
import tr.abdullah.driver.DriverManager;
import tr.abdullah.utils.ElementReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage provides reusable interaction methods integrated with dynamic JSON locator resolution
 * and explicit synchronization strategies.
 */
public class BasePage {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        long explicitWaitSeconds = Long.parseLong(ConfigurationReader.getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSeconds));
    }

    /**
     * Resolves a locator from JSON and waits for the element to become clickable, then clicks it.
     *
     * @param pageName    Name of the JSON file (without extension)
     * @param elementName Key name of the element in the JSON file
     */
    protected void click(String pageName, String elementName) {
        By locator = ElementReader.getLocator(pageName, elementName);
        logger.info("Clicking element [{}.{}]", pageName, elementName);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    /**
     * Resolves locator, clears existing text, and types the provided input.
     *
     * @param pageName    Name of the JSON file
     * @param elementName Key name of the element
     * @param text        Text value to send
     */
    protected void sendKeys(String pageName, String elementName, String text) {
        By locator = ElementReader.getLocator(pageName, elementName);
        logger.info("Typing text into element [{}.{}]", pageName, elementName);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Resolves locator and extracts the visible inner text.
     *
     * @param pageName    Name of the JSON file
     * @param elementName Key name of the element
     * @return Visible text string
     */
    protected String getText(String pageName, String elementName) {
        By locator = ElementReader.getLocator(pageName, elementName);
        logger.info("Retrieving text from element [{}.{}]", pageName, elementName);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText().trim();
    }

    /**
     * Checks whether an element is displayed within the explicit wait timeframe.
     *
     * @param pageName    Name of the JSON file
     * @param elementName Key name of the element
     * @return True if displayed, false otherwise
     */
    protected boolean isDisplayed(String pageName, String elementName) {
        By locator = ElementReader.getLocator(pageName, elementName);
        try {
            logger.info("Checking display status for element [{}.{}]", pageName, elementName);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element [{}.{}] is not visible: {}", pageName, elementName, e.getMessage());
            return false;
        }
    }

    /**
     * Navigates directly to the specified URL.
     *
     * @param url Target destination URL
     */
    public void navigateTo(String url) {
        logger.info("Navigating to URL: [{}]", url);
        driver.get(url);
    }

    /**
     * Retrieves current page URL.
     *
     * @return Current browser URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Retrieves current page title.
     *
     * @return Current browser window title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}