package com.automation.api;

import com.automation.config.ConfigManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * REST API Client using RestAssured
 */
public class RestApiClient {
    private static final Logger logger = LogManager.getLogger(RestApiClient.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private RequestSpecification requestSpec;

    public RestApiClient() {
        initializeRequestSpec();
    }

    /**
     * Initialize request specification with default settings
     */
    private void initializeRequestSpec() {
        requestSpec = RestAssured.given()
                .baseUri(config.getApiBaseUrl())
                .contentType(ContentType.JSON);
        
        logger.info("REST API Client initialized with base URL: {}", config.getApiBaseUrl());
    }

    /**
     * Set base URI
     */
    public RestApiClient setBaseUri(String baseUri) {
        requestSpec.baseUri(baseUri);
        logger.info("Base URI set to: {}", baseUri);
        return this;
    }

    /**
     * Set content type
     */
    public RestApiClient setContentType(ContentType contentType) {
        requestSpec.contentType(contentType);
        logger.info("Content type set to: {}", contentType);
        return this;
    }

    /**
     * Add header
     */
    public RestApiClient addHeader(String name, String value) {
        requestSpec.header(name, value);
        logger.info("Header added: {} = {}", name, value);
        return this;
    }

    /**
     * Add headers
     */
    public RestApiClient addHeaders(Map<String, String> headers) {
        requestSpec.headers(headers);
        logger.info("Headers added: {}", headers);
        return this;
    }

    /**
     * Add headers
     */
    public RestApiClient addHeaders(Headers headers) {
        requestSpec.headers(headers);
        logger.info("Headers added: {}", headers);
        return this;
    }

    /**
     * Add query parameter
     */
    public RestApiClient addQueryParam(String name, String value) {
        requestSpec.queryParam(name, value);
        logger.info("Query parameter added: {} = {}", name, value);
        return this;
    }

    /**
     * Add query parameters
     */
    public RestApiClient addQueryParams(Map<String, String> params) {
        requestSpec.queryParams(params);
        logger.info("Query parameters added: {}", params);
        return this;
    }

    /**
     * Add path parameter
     */
    public RestApiClient addPathParam(String name, String value) {
        requestSpec.pathParam(name, value);
        logger.info("Path parameter added: {} = {}", name, value);
        return this;
    }

    /**
     * Add path parameters
     */
    public RestApiClient addPathParams(Map<String, String> params) {
        requestSpec.pathParams(params);
        logger.info("Path parameters added: {}", params);
        return this;
    }

    /**
     * Set request body
     */
    public RestApiClient setBody(Object body) {
        requestSpec.body(body);
        logger.info("Request body set: {}", body);
        return this;
    }

    /**
     * Set request body as string
     */
    public RestApiClient setBody(String body) {
        requestSpec.body(body);
        logger.info("Request body set: {}", body);
        return this;
    }

    /**
     * Set form parameters
     */
    public RestApiClient setFormParams(Map<String, String> params) {
        requestSpec.formParams(params);
        logger.info("Form parameters set: {}", params);
        return this;
    }

    /**
     * Set form parameter
     */
    public RestApiClient setFormParam(String name, String value) {
        requestSpec.formParam(name, value);
        logger.info("Form parameter set: {} = {}", name, value);
        return this;
    }

    /**
     * Set multipart file
     */
    public RestApiClient setMultiPart(String name, Object file) {
        requestSpec.multiPart(name, file);
        logger.info("Multi-part file set: {} = {}", name, file);
        return this;
    }

    /**
     * Set authentication
     */
    public RestApiClient setAuth(String username, String password) {
        requestSpec.auth().basic(username, password);
        logger.info("Basic authentication set for user: {}", username);
        return this;
    }

    /**
     * Set bearer token
     */
    public RestApiClient setBearerToken(String token) {
        requestSpec.header("Authorization", "Bearer " + token);
        logger.info("Bearer token set");
        return this;
    }

    /**
     * Set API key
     */
    public RestApiClient setApiKey(String apiKey) {
        requestSpec.header("X-API-Key", apiKey);
        logger.info("API key set");
        return this;
    }

    /**
     * GET request
     */
    public Response get(String path) {
        logger.info("Sending GET request to: {}", path);
        Response response = requestSpec.get(path);
        logResponse(response);
        return response;
    }

    /**
     * POST request
     */
    public Response post(String path) {
        logger.info("Sending POST request to: {}", path);
        Response response = requestSpec.post(path);
        logResponse(response);
        return response;
    }

    /**
     * PUT request
     */
    public Response put(String path) {
        logger.info("Sending PUT request to: {}", path);
        Response response = requestSpec.put(path);
        logResponse(response);
        return response;
    }

    /**
     * DELETE request
     */
    public Response delete(String path) {
        logger.info("Sending DELETE request to: {}", path);
        Response response = requestSpec.delete(path);
        logResponse(response);
        return response;
    }

    /**
     * PATCH request
     */
    public Response patch(String path) {
        logger.info("Sending PATCH request to: {}", path);
        Response response = requestSpec.patch(path);
        logResponse(response);
        return response;
    }

    /**
     * HEAD request
     */
    public Response head(String path) {
        logger.info("Sending HEAD request to: {}", path);
        Response response = requestSpec.head(path);
        logResponse(response);
        return response;
    }

    /**
     * OPTIONS request
     */
    public Response options(String path) {
        logger.info("Sending OPTIONS request to: {}", path);
        Response response = requestSpec.options(path);
        logResponse(response);
        return response;
    }

    /**
     * Log response details
     */
    private void logResponse(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.info("Response Status Line: {}", response.getStatusLine());
        logger.info("Response Headers: {}", response.getHeaders());
        logger.info("Response Body: {}", response.getBody().asString());
    }

    /**
     * Get request specification
     */
    public RequestSpecification getRequestSpec() {
        return requestSpec;
    }

    /**
     * Reset request specification
     */
    public RestApiClient reset() {
        initializeRequestSpec();
        logger.info("Request specification reset");
        return this;
    }

    /**
     * Create new instance with custom base URI
     */
    public static RestApiClient withBaseUri(String baseUri) {
        RestApiClient client = new RestApiClient();
        client.setBaseUri(baseUri);
        return client;
    }

    /**
     * Create new instance with custom headers
     */
    public static RestApiClient withHeaders(Map<String, String> headers) {
        RestApiClient client = new RestApiClient();
        client.addHeaders(headers);
        return client;
    }

    /**
     * Create new instance with authentication
     */
    public static RestApiClient withAuth(String username, String password) {
        RestApiClient client = new RestApiClient();
        client.setAuth(username, password);
        return client;
    }

    /**
     * Create new instance with bearer token
     */
    public static RestApiClient withBearerToken(String token) {
        RestApiClient client = new RestApiClient();
        client.setBearerToken(token);
        return client;
    }
} 