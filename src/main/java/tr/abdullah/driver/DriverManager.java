package tr.abdullah.driver;

import tr.abdullah.config.ConfigurationReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

/**
 * Thread-safe WebDriver factory managing browser sessions across parallel threads.
 */
public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Private constructor to prevent direct instantiation
    private DriverManager() {}

    /**
     * Initializes and returns the WebDriver instance for the current thread.
     *
     * @return ThreadLocal WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initDriver();
        }
        return driverThreadLocal.get();
    }

    /**
     * Creates a new WebDriver instance based on browser configuration.
     */
    public static void initDriver() {
        String browser = ConfigurationReader.getProperty("browser");
        if (browser == null) {
            browser = "chrome";
        }
        browser = browser.trim().toLowerCase();

        logger.info("Initializing WebDriver session for browser: [{}] on thread [{}]",
                browser, Thread.currentThread().getId());

        WebDriver driver;

        switch (browser) {
            case "firefox" -> {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
            }
            case "firefox-headless" -> {
                FirefoxOptions headlessFirefox = new FirefoxOptions();
                headlessFirefox.addArguments("-headless");
                driver = new FirefoxDriver(headlessFirefox);
            }
            case "edge" -> {
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
            }
            case "chrome-headless" -> {
                ChromeOptions headlessChrome = new ChromeOptions();
                headlessChrome.addArguments("--headless=new");
                headlessChrome.addArguments("--window-size=1920,1080");
                headlessChrome.addArguments("--disable-gpu");
                headlessChrome.addArguments("--no-sandbox");
                headlessChrome.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(headlessChrome);
            }
            case "chrome" -> {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
            }
            default -> {
                logger.warn("Unsupported browser '{}' supplied. Defaulting to Chrome.", browser);
                ChromeOptions defaultOptions = new ChromeOptions();
                defaultOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(defaultOptions);
            }
        }

        // Configure standard timeouts from config.properties
        long pageLoadTimeout = Long.parseLong(ConfigurationReader.getProperty("page.load.timeout"));
        long implicitWait = Long.parseLong(ConfigurationReader.getProperty("implicit.wait"));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        logger.info("WebDriver successfully initialized and attached to thread [{}]", Thread.currentThread().getId());
    }

    /**
     * Quits the WebDriver session and clears the ThreadLocal reference.
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            logger.info("Terminating WebDriver session for thread [{}]", Thread.currentThread().getId());
            try {
                driver.quit();
            } catch (Exception e) {
                logger.error("Error while quitting WebDriver session: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}