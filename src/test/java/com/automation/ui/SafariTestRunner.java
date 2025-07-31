package com.automation.ui;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Safari-specific Test Runner for UI Tests
 */
@CucumberOptions(
    features = "src/test/resources/features/ui",
    glue = {"com.automation.steps", "com.automation.hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/safari-test-report.html",
        "json:target/cucumber-reports/safari-test-report.json",
        "junit:target/cucumber-reports/safari-test-report.xml"
    },
    tags = "@ui and @smoke",
    monochrome = true,
    dryRun = false
)
public class SafariTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false) // Safari works better sequentially
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 