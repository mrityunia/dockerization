package com.automation.pageobjects;

import com.automation.drivers.DriverManager;
import com.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Base Page Object class with common functionality
 */
public abstract class BasePage {
    protected final Logger logger = LogManager.getLogger(this.getClass());
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;
    protected ScreenshotUtils screenshotUtils;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = DriverManager.getWait();
        this.actions = new Actions(driver);
        this.screenshotUtils = new ScreenshotUtils();
        PageFactory.initElements(driver, this);
    }

    /**
     * Navigate to a specific URL
     */
    public void navigateTo(String url) {
        logger.info("Navigating to URL: {}", url);
        driver.get(url);
    }

    /**
     * Get current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Wait for element to be visible
     */
    public void waitForElementVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be clickable
     */
    public void waitForElementClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be present
     */
    public void waitForElementPresent(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Click on element
     */
    public void click(By locator) {
        logger.info("Clicking on element: {}", locator);
        waitForElementClickable(locator);
        driver.findElement(locator).click();
    }

    /**
     * Click on element with JavaScript
     */
    public void clickWithJS(By locator) {
        logger.info("Clicking on element with JavaScript: {}", locator);
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Send keys to element
     */
    public void sendKeys(By locator, String text) {
        logger.info("Sending keys to element: {} with text: {}", locator, text);
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element
     */
    public String getText(By locator) {
        waitForElementVisible(locator);
        return driver.findElement(locator).getText();
    }

    /**
     * Get attribute value from element
     */
    public String getAttribute(By locator, String attribute) {
        waitForElementPresent(locator);
        return driver.findElement(locator).getAttribute(attribute);
    }

    /**
     * Check if element is displayed
     */
    public boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    public boolean isElementEnabled(By locator) {
        try {
            return driver.findElement(locator).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Select option from dropdown by visible text
     */
    public void selectByVisibleText(By locator, String text) {
        logger.info("Selecting option by visible text: {} from dropdown: {}", text, locator);
        waitForElementVisible(locator);
        Select select = new Select(driver.findElement(locator));
        select.selectByVisibleText(text);
    }

    /**
     * Select option from dropdown by value
     */
    public void selectByValue(By locator, String value) {
        logger.info("Selecting option by value: {} from dropdown: {}", value, locator);
        waitForElementVisible(locator);
        Select select = new Select(driver.findElement(locator));
        select.selectByValue(value);
    }

    /**
     * Select option from dropdown by index
     */
    public void selectByIndex(By locator, int index) {
        logger.info("Selecting option by index: {} from dropdown: {}", index, locator);
        waitForElementVisible(locator);
        Select select = new Select(driver.findElement(locator));
        select.selectByIndex(index);
    }

    /**
     * Get all options from dropdown
     */
    public List<WebElement> getDropdownOptions(By locator) {
        waitForElementVisible(locator);
        Select select = new Select(driver.findElement(locator));
        return select.getOptions();
    }

    /**
     * Hover over element
     */
    public void hoverOver(By locator) {
        logger.info("Hovering over element: {}", locator);
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        actions.moveToElement(element).perform();
    }

    /**
     * Double click on element
     */
    public void doubleClick(By locator) {
        logger.info("Double clicking on element: {}", locator);
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        actions.doubleClick(element).perform();
    }

    /**
     * Right click on element
     */
    public void rightClick(By locator) {
        logger.info("Right clicking on element: {}", locator);
        waitForElementVisible(locator);
        WebElement element = driver.findElement(locator);
        actions.contextClick(element).perform();
    }

    /**
     * Drag and drop element
     */
    public void dragAndDrop(By sourceLocator, By targetLocator) {
        logger.info("Dragging element: {} to: {}", sourceLocator, targetLocator);
        waitForElementVisible(sourceLocator);
        waitForElementVisible(targetLocator);
        WebElement source = driver.findElement(sourceLocator);
        WebElement target = driver.findElement(targetLocator);
        actions.dragAndDrop(source, target).perform();
    }

    /**
     * Switch to frame by index
     */
    public void switchToFrame(int index) {
        logger.info("Switching to frame with index: {}", index);
        driver.switchTo().frame(index);
    }

    /**
     * Switch to frame by name or id
     */
    public void switchToFrame(String nameOrId) {
        logger.info("Switching to frame with name or id: {}", nameOrId);
        driver.switchTo().frame(nameOrId);
    }

    /**
     * Switch to frame by element
     */
    public void switchToFrame(By locator) {
        logger.info("Switching to frame with locator: {}", locator);
        WebElement frameElement = driver.findElement(locator);
        driver.switchTo().frame(frameElement);
    }

    /**
     * Switch to default content
     */
    public void switchToDefaultContent() {
        logger.info("Switching to default content");
        driver.switchTo().defaultContent();
    }

    /**
     * Switch to window by title
     */
    public void switchToWindow(String title) {
        logger.info("Switching to window with title: {}", title);
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().equals(title)) {
                break;
            }
        }
    }

    /**
     * Switch to window by index
     */
    public void switchToWindow(int index) {
        logger.info("Switching to window with index: {}", index);
        String[] windowHandles = driver.getWindowHandles().toArray(new String[0]);
        if (index < windowHandles.length) {
            driver.switchTo().window(windowHandles[index]);
        }
    }

    /**
     * Accept alert
     */
    public void acceptAlert() {
        logger.info("Accepting alert");
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
    }

    /**
     * Dismiss alert
     */
    public void dismissAlert() {
        logger.info("Dismissing alert");
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().dismiss();
    }

    /**
     * Get alert text
     */
    public String getAlertText() {
        logger.info("Getting alert text");
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert().getText();
    }

    /**
     * Send keys to alert
     */
    public void sendKeysToAlert(String text) {
        logger.info("Sending keys to alert: {}", text);
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().sendKeys(text);
    }

    /**
     * Take screenshot
     */
    public void takeScreenshot(String name) {
        screenshotUtils.takeScreenshot(driver, name);
    }

    /**
     * Scroll to element
     */
    public void scrollToElement(By locator) {
        logger.info("Scrolling to element: {}", locator);
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Scroll to top of page
     */
    public void scrollToTop() {
        logger.info("Scrolling to top of page");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    /**
     * Scroll to bottom of page
     */
    public void scrollToBottom() {
        logger.info("Scrolling to bottom of page");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Wait for page to load
     */
    public void waitForPageLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Wait for jQuery to load (if applicable)
     */
    public void waitForJQueryLoad() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return jQuery.active == 0"));
    }
} 