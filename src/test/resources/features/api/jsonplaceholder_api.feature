@api @jsonplaceholder
Feature: JSONPlaceholder API Testing
  As a developer
  I want to test the JSONPlaceholder API endpoints
  So that I can verify the API functionality

  Background:
    Given I have access to the JSONPlaceholder API

  @smoke @get
  Scenario: Get All Posts
    When I send a GET request to "/posts"
    Then the response status code should be 200
    And the response should contain a list of posts
    And each post should have an "id", "title", and "body" field

  @get @single
  Scenario: Get Single Post
    When I send a GET request to "/posts/1"
    Then the response status code should be 200
    And the response should contain a single post
    And the post should have id "1"

  @get @not_found
  Scenario: Get Non-existent Post
    When I send a GET request to "/posts/999"
    Then the response status code should be 404

  @post @create
  Scenario: Create New Post
    When I send a POST request to "/posts" with the following data:
      | title | body                    | userId |
      | Test  | This is a test post     | 1      |
    Then the response status code should be 201
    And the response should contain the created post
    And the post should have an "id" field

  @put @update
  Scenario: Update Existing Post
    When I send a PUT request to "/posts/1" with the following data:
      | title | body                    | userId |
      | Updated | This is an updated post | 1      |
    Then the response status code should be 200
    And the response should contain the updated post
    And the post title should be "Updated"

  @patch @partial_update
  Scenario: Partially Update Post
    When I send a PATCH request to "/posts/1" with the following data:
      | title |
      | Patched |
    Then the response status code should be 200
    And the response should contain the updated post
    And the post title should be "Patched"

  @delete @remove
  Scenario: Delete Post
    When I send a DELETE request to "/posts/1"
    Then the response status code should be 200

  @get @filter
  Scenario: Get Posts by User ID
    When I send a GET request to "/posts" with query parameter "userId=1"
    Then the response status code should be 200
    And all posts should have userId "1"

  @get @comments
  Scenario: Get Comments for a Post
    When I send a GET request to "/posts/1/comments"
    Then the response status code should be 200
    And the response should contain a list of comments
    And all comments should have postId "1"

  @get @users
  Scenario: Get All Users
    When I send a GET request to "/users"
    Then the response status code should be 200
    And the response should contain a list of users
    And each user should have required fields 