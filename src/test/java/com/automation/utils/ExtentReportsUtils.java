package com.automation.utils;

import com.automation.drivers.DriverManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for ExtentReports integration with base64 screenshot capture
 * Automatically captures and logs base64 screenshots when tests fail
 */
public class ExtentReportsUtils {
    private static final Logger logger = LogManager.getLogger(ExtentReportsUtils.class);
    private static final ExtentReports extentReports = new ExtentReports();
    private static final Map<String, ExtentTest> testMap = new HashMap<>();
    private static final ScreenshotUtils screenshotUtils = new ScreenshotUtils();
    
    private static ExtentTest currentTest;
    private static String currentScenarioName;

    static {
        // Initialize ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-reports/extent-report.html");
        extentReports.attachReporter(sparkReporter);
        
        // Set system info
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("Selenium Version", "4.15.0");
        extentReports.setSystemInfo("Framework", "Cucumber + TestNG");
        extentReports.setSystemInfo("Screenshot Format", "Base64 Encoded");
    }

    /**
     * Start a new test in ExtentReports
     */
    public static void startTest(String scenarioName) {
        currentScenarioName = scenarioName;
        currentTest = extentReports.createTest(scenarioName);
        testMap.put(scenarioName, currentTest);
        
        logger.info("ExtentReports test started: {}", scenarioName);
    }

    /**
     * End a test in ExtentReports
     */
    public static void endTest(String scenarioName, boolean passed) {
        ExtentTest test = testMap.get(scenarioName);
        
        if (test != null) {
            if (passed) {
                test.log(Status.PASS, "Test passed successfully");
            } else {
                test.log(Status.FAIL, "Test failed");
                // For failed tests, try to capture screenshot if not already captured
                try {
                    WebDriver driver = getWebDriver();
                    if (driver != null) {
                        captureScreenshotForFailedTest(scenarioName, driver);
                    }
                } catch (Exception e) {
                    logger.error("Failed to capture screenshot for failed test: {}", e.getMessage());
                }
            }
        }
        
        logger.info("ExtentReports test ended: {} - Status: {}", scenarioName, passed ? "PASS" : "FAIL");
    }

    /**
     * Get WebDriver instance
     */
    private static WebDriver getWebDriver() {
        try {
            return DriverManager.getDriver();
        } catch (Exception e) {
            logger.error("Failed to get WebDriver: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Capture and log base64 screenshot for failed test
     */
    public static void captureScreenshotForFailedTest(String scenarioName, WebDriver driver) {
        try {
            if (driver != null) {
                // Take a screenshot as base64
                String screenshotData = screenshotUtils.takeScreenshotAsBase64(driver);
                
                if (screenshotData != null && !screenshotData.isEmpty()) {
                    ExtentTest test = testMap.get(scenarioName);
                    
                    if (test != null) {
                        // Add screenshot to ExtentReports using base64
                        test.addScreenCaptureFromBase64String(screenshotData, "Screenshot for " + scenarioName);
                        
                        // Log screenshot information
                        logger.info("Screenshot captured and attached to ExtentReports for failed test: {} - Size: {} KB", 
                                   scenarioName, screenshotUtils.getScreenshotSizeInKB(screenshotData));
                    }
                }
            } else {
                // For API tests or when no WebDriver is available
                ExtentTest test = testMap.get(scenarioName);
                if (test != null) {
                    test.log(Status.INFO, "No screenshot available - this might be an API test or WebDriver is not initialized");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for failed test: {}", e.getMessage());
        }
    }

    /**
     * Flush ExtentReports
     */
    public static void flushReports() {
        extentReports.flush();
    }

    /**
     * Get current test instance
     */
    public static ExtentTest getCurrentTest(String scenarioName) {
        return testMap.get(scenarioName);
    }
} 