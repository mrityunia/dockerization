@ui @simple
Feature: Simple Test
  As a user
  I want to verify the test framework is working
  So that I can run tests successfully

  @smoke
  Scenario: Basic test verification
    Given I am on the Google homepage
    When I search for "test automation"
    Then I should see search results 