package com.automation.utils;

import com.automation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Utility class for taking screenshots as base64 encoded strings
 * Optimized for database storage and reduced execution size
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Take screenshot and return as base64 encoded string
     * Primary method for database storage
     */
    public String takeScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            String base64Screenshot = ts.getScreenshotAs(OutputType.BASE64);
            logger.info("Screenshot captured as base64 (length: {} chars)", base64Screenshot.length());
            return base64Screenshot;
        } catch (Exception e) {
            logger.error("Failed to take screenshot as base64: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Convert base64 string back to byte array (for retrieval from database)
     */
    public byte[] base64ToBytes(String base64String) {
        try {
            return Base64.getDecoder().decode(base64String);
        } catch (Exception e) {
            logger.error("Failed to convert base64 to bytes: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Get screenshot size in KB
     */
    public double getScreenshotSizeInKB(String base64String) {
        return base64String.length() * 0.75 / 1024.0; // Approximate size in KB
    }

    /**
     * Data class for storing screenshot information
     */
    public static class ScreenshotData {
        private String base64Data;
        private String testName;
        private String stepName;
        private String timestamp;
        private int sizeInBytes;

        // Getters and Setters
        public String getBase64Data() { return base64Data; }
        public void setBase64Data(String base64Data) { this.base64Data = base64Data; }
        
        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        
        public String getStepName() { return stepName; }
        public void setStepName(String stepName) { this.stepName = stepName; }
        
        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
        
        public int getSizeInBytes() { return sizeInBytes; }
        public void setSizeInBytes(int sizeInBytes) { this.sizeInBytes = sizeInBytes; }

        @Override
        public String toString() {
            return String.format("ScreenshotData{testName='%s', stepName='%s', timestamp='%s', size=%d chars}", 
                               testName, stepName, timestamp, base64Data != null ? base64Data.length() : 0);
        }
    }
} 