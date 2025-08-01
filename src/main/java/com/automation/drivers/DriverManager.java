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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe WebDriver Manager for parallel execution
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    
    // Thread-safe maps for parallel execution
    private static final ConcurrentHashMap<Long, WebDriver> driverMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, WebDriverWait> waitMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Long, AtomicBoolean> quitFlags = new ConcurrentHashMap<>();
    
    private static final ConfigManager config = ConfigManager.getInstance();
    
    // Shutdown hook for cleanup
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("JVM shutdown detected, cleaning up all WebDrivers...");
            quitAllDrivers();
        }));
    }

    private DriverManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Initialize WebDriver for current thread (thread-safe)
     */
    public static WebDriver initializeDriver() {
        long threadId = Thread.currentThread().getId();
        String browserName = config.getBrowserName().toLowerCase();
        
        logger.info("Initializing {} browser for thread: {}", browserName, threadId);
        
        // Check if driver already exists for this thread
        if (driverMap.containsKey(threadId)) {
            logger.warn("WebDriver already exists for thread: {}, returning existing instance", threadId);
            return driverMap.get(threadId);
        }
        
        WebDriver driver = createDriver(browserName);
        driverMap.put(threadId, driver);
        
        // Set up WebDriverWait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(config.getExplicitWait()));
        waitMap.put(threadId, wait);
        
        // Initialize quit flag
        quitFlags.put(threadId, new AtomicBoolean(false));
        
        // Configure driver
        configureDriver(driver);
        
        logger.info("WebDriver initialized successfully for thread: {}", threadId);
        return driver;
    }

    /**
     * Get current WebDriver instance for the thread (thread-safe)
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
     * Get WebDriverWait instance for the thread (thread-safe)
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
     * Quit WebDriver for current thread (thread-safe)
     */
    public static void quitDriver() {
        long threadId = Thread.currentThread().getId();
        WebDriver driver = driverMap.get(threadId);
        AtomicBoolean quitFlag = quitFlags.get(threadId);
        
        if (driver != null && quitFlag != null && !quitFlag.get()) {
            // Set quit flag to prevent double quitting
            if (quitFlag.compareAndSet(false, true)) {
                try {
                    logger.info("Quitting WebDriver for thread: {}", threadId);
                    driver.quit();
                    logger.info("WebDriver quit successfully for thread: {}", threadId);
                } catch (Exception e) {
                    logger.warn("Error quitting WebDriver for thread {}: {}", threadId, e.getMessage());
                } finally {
                    // Always remove from maps
                    driverMap.remove(threadId);
                    waitMap.remove(threadId);
                    quitFlags.remove(threadId);
                    logger.info("WebDriver removed from maps for thread: {}", threadId);
                }
            } else {
                logger.debug("WebDriver already being quit for thread: {}", threadId);
            }
        } else {
            logger.debug("No WebDriver found or already quit for thread: {}", threadId);
        }
    }

    /**
     * Quit all WebDrivers (thread-safe)
     */
    public static void quitAllDrivers() {
        logger.info("Quitting all WebDrivers. Total drivers: {}", driverMap.size());
        
        if (driverMap.isEmpty()) {
            logger.info("No WebDrivers to quit");
            return;
        }
        
        // Create a copy of the map to avoid concurrent modification
        ConcurrentHashMap<Long, WebDriver> driversToQuit = new ConcurrentHashMap<>(driverMap);
        
        driversToQuit.forEach((threadId, driver) -> {
            if (driver != null) {
                AtomicBoolean quitFlag = quitFlags.get(threadId);
                if (quitFlag != null && !quitFlag.get()) {
                    if (quitFlag.compareAndSet(false, true)) {
                        try {
                            logger.info("Quitting WebDriver for thread: {}", threadId);
                            driver.quit();
                            logger.info("WebDriver quit successfully for thread: {}", threadId);
                        } catch (Exception e) {
                            logger.warn("Error quitting WebDriver for thread {}: {}", threadId, e.getMessage());
                        } finally {
                            driverMap.remove(threadId);
                            waitMap.remove(threadId);
                            quitFlags.remove(threadId);
                        }
                    }
                }
            }
        });
        
        logger.info("All WebDrivers cleanup completed");
    }

    /**
     * Check if driver exists for current thread
     */
    public static boolean hasDriver() {
        long threadId = Thread.currentThread().getId();
        return driverMap.containsKey(threadId);
    }

    /**
     * Get number of active drivers
     */
    public static int getActiveDriverCount() {
        return driverMap.size();
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
        ChromeOptions options = new ChromeOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless=new");
        }
        
        // Essential Chrome stability options for Selenium Grid
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-plugins");
        options.addArguments("--window-size=" + config.getBrowserWindowSize());
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Chrome cleanup options for better process management
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--disable-hang-monitor");
        options.addArguments("--disable-prompt-on-repost");
        options.addArguments("--disable-sync");
        options.addArguments("--no-first-run");
        options.addArguments("--no-service-autorun");
        options.addArguments("--password-store=basic");
        options.addArguments("--use-mock-keychain");
        
        // Additional cleanup options
        options.addArguments("--disable-background-mode");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-domain-reliability");
        options.addArguments("--disable-sync-preferences");
        options.addArguments("--disable-translate");
        options.addArguments("--metrics-recording-only");
        options.addArguments("--no-default-browser-check");
        options.addArguments("--no-pings");
        
        // Set user agent to avoid detection
        options.addArguments("--user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        
        // Check if we should use Selenium Grid
        String gridUrl = System.getProperty("envUrl");
        if (gridUrl != null && !gridUrl.isEmpty()) {
            try {
                return new RemoteWebDriver(new URL(gridUrl), options);
            } catch (MalformedURLException e) {
                logger.error("Invalid Selenium Grid URL: {}", gridUrl, e);
            }
        }
        
        // Fallback to local WebDriverManager
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        
        if (config.isHeadless()) {
            options.addArguments("--headless");
        }
        
        // Check if we should use Selenium Grid
        String gridUrl = System.getProperty("envUrl");
        if (gridUrl != null && !gridUrl.isEmpty()) {
            try {
                return new RemoteWebDriver(new URL(gridUrl), options);
            } catch (MalformedURLException e) {
                logger.error("Invalid Selenium Grid URL: {}", gridUrl, e);
            }
        }
        
        // Fallback to local WebDriverManager
        WebDriverManager.firefoxdriver().setup();
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