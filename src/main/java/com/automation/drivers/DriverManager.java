package com.automation.drivers;

import com.automation.config.ConfigManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebDriver Manager to handle browser initialization and management
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, WebDriverWait> waitMap = new ConcurrentHashMap<>();
    private static final ConfigManager config = ConfigManager.getInstance();

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize WebDriver based on configuration
     */
    public static WebDriver initializeDriver() {
        long threadId = Thread.currentThread().getId();
        String browserName = config.getBrowserName().toLowerCase();
        
        logger.info("Initializing {} browser for thread: {}", browserName, threadId);
        
        WebDriver driver = createDriver(browserName);
        driverMap.put(threadId, driver);
        
        // Set up WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        waitMap.put(threadId, wait);
        
        // Configure driver
        configureDriver(driver);
        
        logger.info("WebDriver initialized successfully for thread: {}", threadId);
        return driver;
    }

    /**
     * Get current WebDriver instance for the thread
     */
    public static WebDriver getDriver() {
        long threadId = Thread.currentThread().getId();
        WebDriver driver = driverMap.get(threadId);
        
        if (driver == null) {
            logger.warn("No WebDriver found for thread: {}, creating new instance", threadId);
            driver = initializeDriver();
        }
        
        return driver;
    }

    /**
     * Get WebDriverWait instance for the thread
     */
    public static WebDriverWait getWait() {
        long threadId = Thread.currentThread().getId();
        WebDriverWait wait = waitMap.get(threadId);
        
        if (wait == null) {
            logger.warn("No WebDriverWait found for thread: {}, creating new instance", threadId);
            WebDriver driver = getDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
            waitMap.put(threadId, wait);
        }
        
        return wait;
    }

    /**
     * Quit WebDriver for current thread
     */
    public static void quitDriver() {
        long threadId = Thread.currentThread().getId();
        WebDriver driver = driverMap.get(threadId);
        
        if (driver != null) {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", threadId);
            } catch (Exception e) {
                logger.error("Error quitting WebDriver for thread {}: {}", threadId, e.getMessage());
            } finally {
                driverMap.remove(threadId);
                waitMap.remove(threadId);
            }
        }
    }

    /**
     * Quit all WebDrivers
     */
    public static void quitAllDrivers() {
        logger.info("Quitting all WebDrivers");
        driverMap.forEach((threadId, driver) -> {
            try {
                driver.quit();
                logger.info("WebDriver quit successfully for thread: {}", threadId);
            } catch (Exception e) {
                logger.error("Error quitting WebDriver for thread {}: {}", threadId, e.getMessage());
            }
        });
        driverMap.clear();
        waitMap.clear();
    }

    private static WebDriver createDriver(String browserName) {
        switch (browserName) {
            case "chrome":
                return createChromeDriver();
            case "firefox":
                return createFirefoxDriver();
            case "edge":
                return createEdgeDriver();
            case "safari":
                return createSafariDriver();
            default:
                logger.warn("Unsupported browser: {}, defaulting to Chrome", browserName);
                return createChromeDriver();
        }
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        // Chrome performance and stability options
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-images");
        options.addArguments("--window-size=" + config.getBrowserWindowSize());
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Set user agent to avoid detection
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        
        // Experimental options for better performance
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--disable-ipc-flooding-protection");
        
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        return new EdgeDriver(options);
    }

    private static WebDriver createSafariDriver() {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();
        
        // Safari-specific configurations
        options.setCapability("safari.cleanSession", true);
        options.setCapability("safari.useSimulator", false);
        
        return new SafariDriver(options);
    }

    private static void configureDriver(WebDriver driver) {
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        
        // Set page load timeout
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(config.getAppTimeout()));
        
        // Set script timeout
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(config.getAppTimeout()));
        
        // Maximize window if not headless
        if (!config.isHeadless()) {
            driver.manage().window().maximize();
        }
    }
} 