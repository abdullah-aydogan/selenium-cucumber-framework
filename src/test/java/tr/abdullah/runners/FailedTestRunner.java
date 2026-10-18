package tr.abdullah.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "@target/failed_scenarios.txt",
        glue = {"tr.abdullah.stepdefs", "tr.abdullah.hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-rerun.html",
                "json:target/cucumber-reports/cucumber-rerun.json",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        },
        monochrome = true
)
public class FailedTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}