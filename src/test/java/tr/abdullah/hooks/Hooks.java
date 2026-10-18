package tr.abdullah.hooks;

import tr.abdullah.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Cucumber lifecycle hooks managing browser initialization, failure screenshot capture,
 * and session teardown across scenarios.
 */
public class Hooks {

    private static final Logger logger = LogManager.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        logger.info("================================================================================");
        logger.info("Starting Scenario: [{}]", scenario.getName());
        logger.info("================================================================================");
        // Driver initialization triggered via ThreadLocal factory
        DriverManager.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (scenario.isFailed()) {
            logger.error("Scenario [{}] failed. Capturing failure screenshot...", scenario.getName());
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                // Attach screenshot directly to Cucumber HTML report
                scenario.attach(screenshot, "image/png", "Failure_Screenshot");

                // Attach screenshot directly to Allure lifecycle
                Allure.addAttachment("Failure Screenshot - " + scenario.getName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        "png");

                logger.info("Failure screenshot successfully attached to reports.");
            } catch (Exception e) {
                logger.error("Failed to capture failure screenshot: {}", e.getMessage());
            }
        } else {
            logger.info("Scenario [{}] passed successfully.", scenario.getName());
        }

        // Safely close the browser session and clean up ThreadLocal reference
        DriverManager.quitDriver();
        logger.info("WebDriver session terminated for scenario: [{}]\n", scenario.getName());
    }
}