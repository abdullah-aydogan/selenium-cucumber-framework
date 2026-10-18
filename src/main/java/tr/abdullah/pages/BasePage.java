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
 * BasePage provides reusable interaction methods integrated with dynamic JSON locator resolution,
 * parameter formatting, and explicit synchronization strategies.
 */
public abstract class BasePage {

    protected final Logger logger = LogManager.getLogger(getClass());
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        long explicitWaitSeconds = Long.parseLong(ConfigurationReader.getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWaitSeconds));
    }

    protected void click(String pageName, String elementName, Object... params) {
        By locator = ElementReader.getLocator(pageName, elementName, params);
        logger.info("Clicking element [{}.{}]", pageName, elementName);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    protected void sendKeys(String pageName, String elementName, String text, Object... params) {
        By locator = ElementReader.getLocator(pageName, elementName, params);
        logger.info("Typing text into element [{}.{}]", pageName, elementName);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(String pageName, String elementName, Object... params) {
        By locator = ElementReader.getLocator(pageName, elementName, params);
        logger.info("Retrieving text from element [{}.{}]", pageName, elementName);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element.getText().trim();
    }

    protected boolean isDisplayed(String pageName, String elementName, Object... params) {
        By locator = ElementReader.getLocator(pageName, elementName, params);
        try {
            logger.info("Checking display status for element [{}.{}]", pageName, elementName);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (Exception e) {
            logger.debug("Element [{}.{}] is not visible: {}", pageName, elementName, e.getMessage());
            return false;
        }
    }

    public void navigateTo(String url) {
        logger.info("Navigating to URL: [{}]", url);
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }
}