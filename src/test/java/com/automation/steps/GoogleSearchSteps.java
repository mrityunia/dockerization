package com.automation.steps;

import com.automation.config.ConfigManager;
import com.automation.pageobjects.GoogleSearchPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

/**
 * Step definitions for Google Search functionality
 */
public class GoogleSearchSteps {
    private static final Logger logger = LogManager.getLogger(GoogleSearchSteps.class);
    private GoogleSearchPage googleSearchPage;
    private ConfigManager config = ConfigManager.getInstance();

    public GoogleSearchSteps() {
        this.googleSearchPage = new GoogleSearchPage();
    }

    @Given("I am on the Google homepage")
    public void i_am_on_the_google_homepage() {
        logger.info("Navigating to Google homepage");
        googleSearchPage.navigateTo(config.getAppBaseUrl());
        googleSearchPage.waitForPageLoad();
    }

    @When("I search for {string}")
    public void i_search_for(String searchTerm) {
        logger.info("Searching for: {}", searchTerm);
        googleSearchPage.search(searchTerm);
    }

    @When("I type {string} in the search box")
    public void i_type_in_the_search_box(String searchTerm) {
        logger.info("Typing in search box: {}", searchTerm);
        googleSearchPage.typeInSearchBox(searchTerm);
    }

    @When("I clear the search")
    public void i_clear_the_search() {
        logger.info("Clearing the search");
        googleSearchPage.clearSearch();
    }

    @Then("I should see search results")
    public void i_should_see_search_results() {
        logger.info("Verifying search results are displayed");
        Assert.assertTrue("Search results should be displayed", 
                         googleSearchPage.areSearchResultsDisplayed());
    }

    @Then("I should see multiple search results")
    public void i_should_see_multiple_search_results() {
        logger.info("Verifying multiple search results are displayed");
        Assert.assertTrue("Multiple search results should be displayed", 
                         googleSearchPage.getSearchResultsCount() > 1);
    }

    @Then("the first result should contain {string}")
    public void the_first_result_should_contain(String expectedText) {
        logger.info("Verifying first result contains: {}", expectedText);
        String firstResultText = googleSearchPage.getFirstSearchResultText();
        Assert.assertTrue("First result should contain: " + expectedText,
                         firstResultText.toLowerCase().contains(expectedText.toLowerCase()));
    }

    @Then("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedText) {
        logger.info("Verifying page title contains: {}", expectedText);
        String pageTitle = googleSearchPage.getPageTitle();
        Assert.assertTrue("Page title should contain: " + expectedText,
                         pageTitle.toLowerCase().contains(expectedText.toLowerCase()));
    }

    @Then("I should see a message indicating no results found")
    public void i_should_see_a_message_indicating_no_results_found() {
        logger.info("Verifying no results message is displayed");
        Assert.assertTrue("No results message should be displayed",
                         googleSearchPage.isNoResultsMessageDisplayed());
    }

    @Then("I should see search suggestions")
    public void i_should_see_search_suggestions() {
        logger.info("Verifying search suggestions are displayed");
        Assert.assertTrue("Search suggestions should be displayed",
                         googleSearchPage.areSearchSuggestionsDisplayed());
    }

    @Then("the suggestions should be related to {string}")
    public void the_suggestions_should_be_related_to(String searchTerm) {
        logger.info("Verifying suggestions are related to: {}", searchTerm);
        Assert.assertTrue("Suggestions should be related to: " + searchTerm,
                         googleSearchPage.areSuggestionsRelatedTo(searchTerm));
    }

    @Then("I should see results containing the exact phrase")
    public void i_should_see_results_containing_the_exact_phrase() {
        logger.info("Verifying results contain exact phrase");
        Assert.assertTrue("Results should contain exact phrase",
                         googleSearchPage.doResultsContainExactPhrase());
    }

    @Then("the search box should be empty")
    public void the_search_box_should_be_empty() {
        logger.info("Verifying search box is empty");
        Assert.assertTrue("Search box should be empty",
                         googleSearchPage.isSearchBoxEmpty());
    }

    @Then("I should be back on the homepage")
    public void i_should_be_back_on_the_homepage() {
        logger.info("Verifying we are back on the homepage");
        String currentUrl = googleSearchPage.getCurrentUrl();
        Assert.assertTrue("Should be back on homepage",
                         currentUrl.equals(config.getAppBaseUrl()) || 
                         currentUrl.equals(config.getAppBaseUrl() + "/"));
    }
} 