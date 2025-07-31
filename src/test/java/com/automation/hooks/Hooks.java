package com.automation.hooks;

import com.automation.drivers.DriverManager;
import com.automation.utils.ScreenshotUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber Hooks for setup and teardown operations
 */
public class Hooks {
    private static final Logger logger = LogManager.getLogger(Hooks.class);
    private ScreenshotUtils screenshotUtils;

    public Hooks() {
        this.screenshotUtils = new ScreenshotUtils();
    }

    /**
     * Setup before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting scenario: {}", scenario.getName());
        
        // Initialize WebDriver if not already initialized
        WebDriver driver = DriverManager.getDriver();
        
        // Log scenario details
        logger.info("Scenario Tags: {}", scenario.getSourceTagNames());
        logger.info("Scenario Line: {}", scenario.getLine());
    }

    /**
     * Cleanup after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        logger.info("Finishing scenario: {}", scenario.getName());
        
        // Take screenshot if scenario failed
        if (scenario.isFailed()) {
            logger.info("Scenario failed, taking screenshot");
            try {
                WebDriver driver = DriverManager.getDriver();
                String screenshotName = "failed_" + scenario.getName().replaceAll("\\s+", "_");
                screenshotUtils.takeScreenshot(driver, screenshotName);
                
                // Attach screenshot to Cucumber report
                byte[] screenshotBytes = screenshotUtils.takeScreenshotAsBytes(driver);
                scenario.attach(screenshotBytes, "image/png", screenshotName + ".png");
                
                logger.info("Screenshot attached to scenario report");
            } catch (Exception e) {
                logger.error("Failed to take screenshot: {}", e.getMessage());
            }
        }
        
        // Quit WebDriver for current thread
        DriverManager.quitDriver();
        
        logger.info("Scenario completed: {} - Status: {}", 
                   scenario.getName(), 
                   scenario.getStatus());
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
        // Quit all remaining WebDrivers
        DriverManager.quitAllDrivers();
    }

    /**
     * Setup before each step
     */
    @Before(order = 2)
    public void beforeStep() {
        // Additional step-level setup if needed
    }

    /**
     * Cleanup after each step
     */
    @After(order = 2)
    public void afterStep() {
        // Additional step-level cleanup if needed
    }
} 