package com.automation.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNG Test Runner for API Tests
 */
@CucumberOptions(
    features = "src/test/resources/features/api",
    glue = {"com.automation.steps", "com.automation.hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/api-test-report.html",
        "json:target/cucumber-reports/api-test-report.json",
        "junit:target/cucumber-reports/api-test-report.xml"
    },
    tags = "@api",
    monochrome = true,
    dryRun = false
)
public class APITestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 