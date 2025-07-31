@ui @google
Feature: Google Search Functionality
  As a user
  I want to search for information on Google
  So that I can find relevant results

  Background:
    Given I am on the Google homepage

  @smoke @search
  Scenario: Basic Google Search
    When I search for "Selenium WebDriver"
    Then I should see search results
    And the first result should contain "Selenium"

  @search @multiple_results
  Scenario: Search with Multiple Results
    When I search for "Java programming"
    Then I should see multiple search results
    And the page title should contain "Java programming"

  @search @no_results
  Scenario: Search with No Results
    When I search for "xyz123nonexistentterm"
    Then I should see a message indicating no results found

  @search @suggestions
  Scenario: Search Suggestions
    When I type "selenium" in the search box
    Then I should see search suggestions
    And the suggestions should be related to "selenium"

  @search @advanced
  Scenario: Advanced Search with Quotes
    When I search for "exact phrase search"
    Then I should see results containing the exact phrase

  @search @clear
  Scenario: Clear Search Results
    When I search for "test automation"
    And I clear the search
    Then the search box should be empty
    And I should be back on the homepage 