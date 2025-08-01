package com.automation;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Unified TestNG Test Runner for All Tests (UI + API)
 * Supports both sequential and parallel execution
 * Generates a single comprehensive report for all test scenarios
 */
@CucumberOptions(
    features = {
        "src/test/resources/features/ui",
        "src/test/resources/features/api"
    },
    glue = {
        "com.automation.steps", 
        "com.automation.hooks.Hooks",
        "com.automation.hooks.APIHooks"
    },
    plugin = {
        "pretty",
        "html:target/cucumber-reports/unified-test-report.html",
        "json:target/cucumber-reports/unified-test-report.json",
        "junit:target/cucumber-reports/unified-test-report.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    dryRun = false
)
public class UnifiedTestRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
} 