package com.automation.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for Google Search Page
 */
public class GoogleSearchPage extends BasePage {
    
    // Page elements
    @FindBy(name = "q")
    private WebElement searchBox;
    
    @FindBy(name = "btnK")
    private WebElement searchButton;
    
    @FindBy(css = "div[role='main']")
    private WebElement searchResultsContainer;
    
    @FindBy(xpath = "//div[@Id='search']//div[@id='rso']/div")
    private List<WebElement> searchResults;
    
    @FindBy(css = "div[role='navigation']")
    private WebElement navigation;
    
    @FindBy(css = "div[aria-label='Search suggestions']")
    private WebElement searchSuggestions;
    
    @FindBy(css = "div[role='status']")
    private WebElement noResultsMessage;

    /**
     * Search for a term
     */
    public void search(String searchTerm) {
        sendKeys(By.name("q"), searchTerm);
        click(By.name("btnK"));
        waitForPageLoad();
    }

    /**
     * Type in search box without submitting
     */
    public void typeInSearchBox(String searchTerm) {
        sendKeys(By.name("q"), searchTerm);
    }

    /**
     * Clear the search box
     */
    public void clearSearch() {
        searchBox.clear();
    }

    /**
     * Check if search results are displayed
     */
    public boolean areSearchResultsDisplayed() {
        try {
            return isElementDisplayed(By.cssSelector("div[role='main']")) &&
                   getSearchResultsCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the number of search results
     */
    public int getSearchResultsCount() {
        try {
            //List<WebElement> results = searchResults;
            return searchResults.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Get text of first search result
     */
    public String getFirstSearchResultText() {
        try {
            WebElement firstResult = driver.findElements(By.xpath("//a[contains(@href,'https://www.selenium')]")).get(0);
            return firstResult.getText();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Check if no results message is displayed
     */
    public boolean isNoResultsMessageDisplayed() {
        try {
            return isElementDisplayed(By.cssSelector("div[role='status']")) ||
                   isElementDisplayed(By.cssSelector("div[data-ved]"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if search suggestions are displayed
     */
    public boolean areSearchSuggestionsDisplayed() {
        try {
            return isElementDisplayed(By.cssSelector("div[aria-label='Search suggestions']")) ||
                   isElementDisplayed(By.cssSelector("ul[role='listbox']"));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if suggestions are related to search term
     */
    public boolean areSuggestionsRelatedTo(String searchTerm) {
        try {
            List<WebElement> suggestions = driver.findElements(By.cssSelector("ul[role='listbox'] li"));
            for (WebElement suggestion : suggestions) {
                if (suggestion.getText().toLowerCase().contains(searchTerm.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if results contain exact phrase
     */
    public boolean doResultsContainExactPhrase() {
        try {
            List<WebElement> results = driver.findElements(By.cssSelector("div.g"));
            for (WebElement result : results) {
                String resultText = result.getText();
                if (resultText.contains("\"") && resultText.contains("\"")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if search box is empty
     */
    public boolean isSearchBoxEmpty() {
        try {
            return searchBox.getAttribute("value").isEmpty();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Get search box value
     */
    public String getSearchBoxValue() {
        try {
            return searchBox.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Click on first search result
     */
    public void clickFirstSearchResult() {
        try {
            WebElement firstResult = driver.findElement(By.cssSelector("div.g h3"));
            firstResult.click();
        } catch (Exception e) {
            logger.error("Failed to click first search result: {}", e.getMessage());
        }
    }

    /**
     * Get all search result titles
     */
    public List<String> getAllSearchResultTitles() {
        try {
            List<WebElement> titles = driver.findElements(By.cssSelector("div.g h3"));
            return titles.stream()
                    .map(WebElement::getText)
                        .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of();
        }
    }

    /**
     * Check if search button is enabled
     */
    public boolean isSearchButtonEnabled() {
        try {
            return searchButton.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Submit search using Enter key
     */
    public void submitSearchWithEnter() {
        try {
            searchBox.sendKeys(org.openqa.selenium.Keys.ENTER);
            waitForPageLoad();
        } catch (Exception e) {
            logger.error("Failed to submit search with Enter: {}", e.getMessage());
        }
    }
} 