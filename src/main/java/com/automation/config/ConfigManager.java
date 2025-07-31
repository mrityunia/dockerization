package com.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager to handle all framework configuration
 */
public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager() {
        loadProperties();
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
            logger.info("Configuration properties loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading configuration properties: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            logger.warn("Could not parse property {} as integer, using default value: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    // Application Configuration
    public String getAppBaseUrl() {
        return getProperty("app.base.url");
    }

    public int getAppTimeout() {
        return getIntProperty("app.timeout", 30);
    }

    public int getImplicitWait() {
        return getIntProperty("app.implicit.wait", 10);
    }

    public int getExplicitWait() {
        return getIntProperty("app.explicit.wait", 20);
    }

    // Browser Configuration
    public String getBrowserName() {
        return getProperty("browser.name", "chrome");
    }

    public boolean isHeadless() {
        return getBooleanProperty("browser.headless", false);
    }

    public String getBrowserWindowSize() {
        return getProperty("browser.window.size", "1920x1080");
    }

    public String getDownloadPath() {
        return getProperty("browser.download.path", "downloads/");
    }

    // API Configuration
    public String getApiBaseUrl() {
        return getProperty("api.base.url");
    }

    public int getApiTimeout() {
        return getIntProperty("api.timeout", 30);
    }

    public int getApiConnectionTimeout() {
        return getIntProperty("api.connection.timeout", 10000);
    }

    public int getApiReadTimeout() {
        return getIntProperty("api.read.timeout", 30000);
    }

    // Test Configuration
    public String getTestDataPath() {
        return getProperty("test.data.path", "src/test/resources/testdata/");
    }

    public String getTestReportsPath() {
        return getProperty("test.reports.path", "target/cucumber-reports/");
    }

    public String getScreenshotsPath() {
        return getProperty("test.screenshots.path", "screenshots/");
    }

    // Parallel Execution Configuration
    public int getParallelThreadCount() {
        return getIntProperty("parallel.thread.count", 4);
    }

    public boolean isParallelExecutionEnabled() {
        return getBooleanProperty("parallel.execution.enabled", true);
    }

    // Screenshot Configuration
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public boolean isScreenshotOnSuccess() {
        return getBooleanProperty("screenshot.on.success", false);
    }

    public String getScreenshotFormat() {
        return getProperty("screenshot.format", "png");
    }

    // Retry Configuration
    public int getRetryCount() {
        return getIntProperty("retry.count", 2);
    }

    public int getRetryDelay() {
        return getIntProperty("retry.delay", 1000);
    }
} 