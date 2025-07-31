package com.automation.utils;

import com.automation.config.ConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for taking screenshots
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final ConfigManager config = ConfigManager.getInstance();

    /**
     * Take screenshot and save to file
     */
    public void takeScreenshot(WebDriver driver, String name) {
        try {
            // Create screenshots directory if it doesn't exist
            String screenshotPath = config.getScreenshotsPath();
            Path directory = Paths.get(screenshotPath);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("%s_%s.%s", name, timestamp, config.getScreenshotFormat());
            Path filePath = directory.resolve(filename);

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File screenshot = ts.getScreenshotAs(OutputType.FILE);
            
            // Copy to destination
            Files.copy(screenshot.toPath(), filePath);
            
            logger.info("Screenshot saved: {}", filePath.toString());
        } catch (IOException e) {
            logger.error("Failed to take screenshot: {}", e.getMessage());
        }
    }

    /**
     * Take screenshot and return as byte array
     */
    public byte[] takeScreenshotAsBytes(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error("Failed to take screenshot as bytes: {}", e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Take screenshot and return as base64 string
     */
    public String takeScreenshotAsBase64(WebDriver driver) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            return ts.getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to take screenshot as base64: {}", e.getMessage());
            return "";
        }
    }
} 