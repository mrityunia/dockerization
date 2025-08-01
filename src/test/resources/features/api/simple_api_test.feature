@api @simple
Feature: Simple API Test
  As a developer
  I want to verify API endpoints
  So that I can ensure the API is working correctly

  @get
  Scenario: Verify JSONPlaceholder API
    Given I want to test the JSONPlaceholder API
    When I send a GET request to "/posts/1"
    Then the response status should be 200
    And the response should contain "userId" 