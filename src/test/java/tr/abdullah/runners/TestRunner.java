package tr.abdullah.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestRunner bootstraps the Cucumber framework with TestNG,
 * configuring feature paths, glue packages, reporting plugins, and parallel execution.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"tr.abdullah.stepdefs", "tr.abdullah.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "rerun:target/failed_scenarios.txt",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

    /**
     * Overriding scenarios data provider to enable thread-level parallel scenario execution.
     */
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}