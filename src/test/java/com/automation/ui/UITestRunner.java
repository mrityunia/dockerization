package com.automation.ui;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * TestNG Test Runner for UI Tests
 */
@CucumberOptions(
    features = "src/test/resources/features/ui",
    glue = {"com.automation.steps", "com.automation.hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/ui-test-report.html",
        "json:target/cucumber-reports/ui-test-report.json",
        "junit:target/cucumber-reports/ui-test-report.xml"
    },
    tags = "@ui",
    monochrome = true,
    dryRun = false
)
public class UITestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 