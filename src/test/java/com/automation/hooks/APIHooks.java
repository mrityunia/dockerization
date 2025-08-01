package com.automation.hooks;

import com.automation.utils.ExtentReportsUtils;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Cucumber Hooks for API Tests (No WebDriver initialization)
 */
public class APIHooks {
    private static final Logger logger = LogManager.getLogger(APIHooks.class);

    /**
     * Setup before each API scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        logger.info("Starting API scenario: {}", scenario.getName());
        
        // Start ExtentReports test for API scenario
        ExtentReportsUtils.startTest(scenario.getName());
    }

    /**
     * Cleanup after each API scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        logger.info("Finishing API scenario: {}", scenario.getName());
        
        boolean testPassed = !scenario.isFailed();
        
        // End ExtentReports test for API scenario
        ExtentReportsUtils.endTest(scenario.getName(), !testPassed);
        
        logger.info("API scenario completed: {} - Status: {}", 
                   scenario.getName(), 
                   scenario.getStatus());
    }

    /**
     * Setup before all API scenarios (runs once)
     */
    @Before(order = 1)
    public void beforeAll() {
        logger.info("Starting API test execution");
    }

    /**
     * Cleanup after all API scenarios (runs once)
     */
    @After(order = 1)
    public void afterAll() {
        logger.info("API test execution completed");
    }
} 