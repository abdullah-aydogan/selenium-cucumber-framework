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
import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe WebDriver factory managing browser sessions across parallel threads.
 */
public class DriverManager {

    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initDriver();
        }
        return driverThreadLocal.get();
    }

    public static void initDriver() {
        // Priority: Command Line (-Dbrowser=...) > config.properties > default (chrome)
        String browser = System.getProperty("browser");
        if (browser == null || browser.trim().isEmpty()) {
            browser = ConfigurationReader.getProperty("browser");
        }
        if (browser == null || browser.trim().isEmpty()) {
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
                ChromeOptions headlessChrome = createChromeOptions();
                headlessChrome.addArguments("--headless=new");
                headlessChrome.addArguments("--window-size=1920,1080");
                headlessChrome.addArguments("--disable-gpu");
                headlessChrome.addArguments("--no-sandbox");
                headlessChrome.addArguments("--disable-dev-shm-usage");
                driver = new ChromeDriver(headlessChrome);
            }
            case "chrome" -> {
                ChromeOptions chromeOptions = createChromeOptions();
                driver = new ChromeDriver(chromeOptions);
            }
            default -> {
                logger.warn("Unsupported browser '{}' supplied. Defaulting to Chrome.", browser);
                ChromeOptions defaultOptions = createChromeOptions();
                driver = new ChromeDriver(defaultOptions);
            }
        }

        long pageLoadTimeout = Long.parseLong(ConfigurationReader.getProperty("page.load.timeout"));
        long implicitWait = Long.parseLong(ConfigurationReader.getProperty("implicit.wait"));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        logger.info("WebDriver successfully initialized and attached to thread [{}]", Thread.currentThread().getId());
    }

    /**
     * Builds ChromeOptions with security alerts, password bubbles, and leak checks disabled.
     */
    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // Disable password save prompts and leak detection alerts
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-save-password-bubble");
        options.addArguments("--disable-features=PasswordLeakDetection");
        options.addArguments("--remote-allow-origins=*");

        return options;
    }

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