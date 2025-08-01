package com.automation.hooks;

import com.automation.drivers.DriverManager;
import com.automation.utils.ExtentReportsUtils;
import com.automation.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber Hooks for WebDriver management and ExtentReports integration
 * Handles setup, teardown, and screenshot captures for failed scenarios
 */
public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private static final ScreenshotUtils screenshotUtils = new ScreenshotUtils();

    /**
     * Setup before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        long threadId = Thread.currentThread().getId();
        logger.info("Starting scenario: {} on thread: {}", scenario.getName(), threadId);
        
        // Start ExtentReports test
        ExtentReportsUtils.startTest(scenario.getName());
    }

    /**
     * Cleanup after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        long threadId = Thread.currentThread().getId();
        logger.info("Finishing scenario: {} on thread: {}", scenario.getName(), threadId);
        
        boolean testPassed = !scenario.isFailed();

        // End ExtentReports test
        ExtentReportsUtils.endTest(scenario.getName(), testPassed);
        
        // Cleanup WebDriver
        try {
            DriverManager.quitDriver();
        } catch (Exception e) {
            logger.error("Error during driver cleanup: {}", e.getMessage());
        }
        
        logger.info("Scenario completed: {} - Status: {} - Thread: {}", 
                   scenario.getName(), scenario.getStatus(), threadId);
    }

    /**
     * Setup before all scenarios (runs once)
     */
    @Before(order = 1)
    public void beforeAll() {
        logger.info("Starting test execution");
    }

    /**
     * Cleanup after all scenarios (runs once)
     */
    @After(order = 1)
    public void afterAll() {
        logger.info("Test execution completed");
        
        // Flush ExtentReports
        ExtentReportsUtils.flushReports();
        
        // Cleanup all WebDrivers
        try {
            DriverManager.quitAllDrivers();
            logger.info("All WebDrivers quit successfully");
        } catch (Exception e) {
            logger.error("Error quitting all WebDrivers: {}", e.getMessage());
        }
    }
} 