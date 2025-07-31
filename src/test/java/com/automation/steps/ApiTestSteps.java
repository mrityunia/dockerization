package com.automation.steps;

import com.automation.api.RestApiClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

/**
 * Step definitions for API testing
 */
public class ApiTestSteps {
    private static final Logger logger = LogManager.getLogger(ApiTestSteps.class);
    private RestApiClient apiClient;
    private Response response;

    public ApiTestSteps() {
        this.apiClient = new RestApiClient();
    }

    @Given("I have access to the JSONPlaceholder API")
    public void i_have_access_to_the_jsonplaceholder_api() {
        logger.info("Setting up API client for JSONPlaceholder");
        // API client is already initialized with the base URL from config
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        logger.info("Sending GET request to: {}", endpoint);
        response = apiClient.get(endpoint);
    }

    @When("I send a POST request to {string} with the following data:")
    public void i_send_a_post_request_to_with_the_following_data(String endpoint, DataTable dataTable) {
        logger.info("Sending POST request to: {} with data", endpoint);
        Map<String, String> requestData = dataTable.asMap(String.class, String.class);
        apiClient.setBody(requestData);
        response = apiClient.post(endpoint);
    }

    @When("I send a PUT request to {string} with the following data:")
    public void i_send_a_put_request_to_with_the_following_data(String endpoint, DataTable dataTable) {
        logger.info("Sending PUT request to: {} with data", endpoint);
        Map<String, String> requestData = dataTable.asMap(String.class, String.class);
        apiClient.setBody(requestData);
        response = apiClient.put(endpoint);
    }

    @When("I send a PATCH request to {string} with the following data:")
    public void i_send_a_patch_request_to_with_the_following_data(String endpoint, DataTable dataTable) {
        logger.info("Sending PATCH request to: {} with data", endpoint);
        Map<String, String> requestData = dataTable.asMap(String.class, String.class);
        apiClient.setBody(requestData);
        response = apiClient.patch(endpoint);
    }

    @When("I send a DELETE request to {string}")
    public void i_send_a_delete_request_to(String endpoint) {
        logger.info("Sending DELETE request to: {}", endpoint);
        response = apiClient.delete(endpoint);
    }

    @When("I send a GET request to {string} with query parameter {string}")
    public void i_send_a_get_request_to_with_query_parameter(String endpoint, String queryParam) {
        logger.info("Sending GET request to: {} with query parameter: {}", endpoint, queryParam);
        String[] parts = queryParam.split("=");
        if (parts.length == 2) {
            apiClient.addQueryParam(parts[0], parts[1]);
        }
        response = apiClient.get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        logger.info("Verifying response status code: {}", expectedStatusCode);
        Assert.assertEquals("Response status code should be " + expectedStatusCode,
                           expectedStatusCode, response.getStatusCode());
    }

    @Then("the response should contain a list of posts")
    public void the_response_should_contain_a_list_of_posts() {
        logger.info("Verifying response contains a list of posts");
        List<Map<String, Object>> posts = response.jsonPath().getList("$");
        Assert.assertNotNull("Response should contain a list", posts);
        Assert.assertTrue("Response should contain posts", posts.size() > 0);
    }

    @Then("each post should have an {string}, {string}, and {string} field")
    public void each_post_should_have_required_fields(String field1, String field2, String field3) {
        logger.info("Verifying each post has required fields: {}, {}, {}", field1, field2, field3);
        List<Map<String, Object>> posts = response.jsonPath().getList("$");
        
        for (Map<String, Object> post : posts) {
            Assert.assertTrue("Post should have " + field1, post.containsKey(field1));
            Assert.assertTrue("Post should have " + field2, post.containsKey(field2));
            Assert.assertTrue("Post should have " + field3, post.containsKey(field3));
        }
    }

    @Then("the response should contain a single post")
    public void the_response_should_contain_a_single_post() {
        logger.info("Verifying response contains a single post");
        Map<String, Object> post = response.jsonPath().getMap("$");
        Assert.assertNotNull("Response should contain a single post", post);
    }

    @Then("the post should have id {string}")
    public void the_post_should_have_id(String expectedId) {
        logger.info("Verifying post has id: {}", expectedId);
        String actualId = response.jsonPath().getString("id");
        Assert.assertEquals("Post should have id " + expectedId, expectedId, actualId);
    }

    @Then("the response should contain the created post")
    public void the_response_should_contain_the_created_post() {
        logger.info("Verifying response contains the created post");
        Map<String, Object> post = response.jsonPath().getMap("$");
        Assert.assertNotNull("Response should contain the created post", post);
    }

    @Then("the post should have an {string} field")
    public void the_post_should_have_an_id_field(String fieldName) {
        logger.info("Verifying post has field: {}", fieldName);
        Object fieldValue = response.jsonPath().get(fieldName);
        Assert.assertNotNull("Post should have " + fieldName + " field", fieldValue);
    }

    @Then("the response should contain the updated post")
    public void the_response_should_contain_the_updated_post() {
        logger.info("Verifying response contains the updated post");
        Map<String, Object> post = response.jsonPath().getMap("$");
        Assert.assertNotNull("Response should contain the updated post", post);
    }

    @Then("the post title should be {string}")
    public void the_post_title_should_be(String expectedTitle) {
        logger.info("Verifying post title is: {}", expectedTitle);
        String actualTitle = response.jsonPath().getString("title");
        Assert.assertEquals("Post title should be " + expectedTitle, expectedTitle, actualTitle);
    }

    @Then("all posts should have userId {string}")
    public void all_posts_should_have_user_id(String expectedUserId) {
        logger.info("Verifying all posts have userId: {}", expectedUserId);
        List<Map<String, Object>> posts = response.jsonPath().getList("$");
        
        for (Map<String, Object> post : posts) {
            String actualUserId = post.get("userId").toString();
            Assert.assertEquals("Post should have userId " + expectedUserId, 
                              expectedUserId, actualUserId);
        }
    }

    @Then("the response should contain a list of comments")
    public void the_response_should_contain_a_list_of_comments() {
        logger.info("Verifying response contains a list of comments");
        List<Map<String, Object>> comments = response.jsonPath().getList("$");
        Assert.assertNotNull("Response should contain a list of comments", comments);
        Assert.assertTrue("Response should contain comments", comments.size() > 0);
    }

    @Then("all comments should have postId {string}")
    public void all_comments_should_have_post_id(String expectedPostId) {
        logger.info("Verifying all comments have postId: {}", expectedPostId);
        List<Map<String, Object>> comments = response.jsonPath().getList("$");
        
        for (Map<String, Object> comment : comments) {
            String actualPostId = comment.get("postId").toString();
            Assert.assertEquals("Comment should have postId " + expectedPostId, 
                              expectedPostId, actualPostId);
        }
    }

    @Then("the response should contain a list of users")
    public void the_response_should_contain_a_list_of_users() {
        logger.info("Verifying response contains a list of users");
        List<Map<String, Object>> users = response.jsonPath().getList("$");
        Assert.assertNotNull("Response should contain a list of users", users);
        Assert.assertTrue("Response should contain users", users.size() > 0);
    }

    @Then("each user should have required fields")
    public void each_user_should_have_required_fields() {
        logger.info("Verifying each user has required fields");
        List<Map<String, Object>> users = response.jsonPath().getList("$");
        String[] requiredFields = {"id", "name", "username", "email"};
        
        for (Map<String, Object> user : users) {
            for (String field : requiredFields) {
                Assert.assertTrue("User should have " + field, user.containsKey(field));
            }
        }
    }

    @Then("the response body should not be empty")
    public void the_response_body_should_not_be_empty() {
        logger.info("Verifying response body is not empty");
        String responseBody = response.getBody().asString();
        Assert.assertFalse("Response body should not be empty", responseBody.isEmpty());
    }

    @Then("the response should contain {string}")
    public void the_response_should_contain(String expectedText) {
        logger.info("Verifying response contains: {}", expectedText);
        String responseBody = response.getBody().asString();
        Assert.assertTrue("Response should contain " + expectedText,
                         responseBody.contains(expectedText));
    }
} 