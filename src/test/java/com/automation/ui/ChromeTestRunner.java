package com.automation.ui;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Chrome-specific Test Runner for UI Tests
 */
@CucumberOptions(
    features = "src/test/resources/features/ui",
    glue = {"com.automation.steps", "com.automation.hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/chrome-test-report.html",
        "json:target/cucumber-reports/chrome-test-report.json",
        "junit:target/cucumber-reports/chrome-test-report.xml"
    },
    tags = "@ui",
    monochrome = true,
    dryRun = false
)
public class ChromeTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true) // Chrome works well with parallel execution
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 