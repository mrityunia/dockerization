package com.automation.testdata;

import com.automation.config.ConfigManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Test Data Manager for handling test data from various sources
 */
public class TestDataManager {
    private static final Logger logger = LogManager.getLogger(TestDataManager.class);
    private static final ConfigManager config = ConfigManager.getInstance();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    private static TestDataManager instance;
    private Map<String, Object> testData;

    private TestDataManager() {
        testData = new HashMap<>();
        loadTestData();
    }

    public static TestDataManager getInstance() {
        if (instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }

    /**
     * Load test data from various sources
     */
    private void loadTestData() {
        loadPropertiesTestData();
        loadJsonTestData();
        loadEnvironmentVariables();
    }

    /**
     * Load test data from properties files
     */
    private void loadPropertiesTestData() {
        try {
            String testDataPath = config.getTestDataPath();
            File testDataDir = new File(testDataPath);
            
            if (testDataDir.exists() && testDataDir.isDirectory()) {
                File[] propertyFiles = testDataDir.listFiles((dir, name) -> name.endsWith(".properties"));
                
                if (propertyFiles != null) {
                    for (File file : propertyFiles) {
                        Properties props = new Properties();
                        try (InputStream input = new FileInputStream(file)) {
                            props.load(input);
                            String fileName = file.getName().replace(".properties", "");
                            testData.put(fileName, props);
                            logger.info("Loaded test data from: {}", file.getName());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Error loading properties test data: {}", e.getMessage());
        }
    }

    /**
     * Load test data from JSON files
     */
    private void loadJsonTestData() {
        try {
            String testDataPath = config.getTestDataPath();
            File testDataDir = new File(testDataPath);
            
            if (testDataDir.exists() && testDataDir.isDirectory()) {
                File[] jsonFiles = testDataDir.listFiles((dir, name) -> name.endsWith(".json"));
                
                if (jsonFiles != null) {
                    for (File file : jsonFiles) {
                        try {
                            Object jsonData = objectMapper.readValue(file, Object.class);
                            String fileName = file.getName().replace(".json", "");
                            testData.put(fileName, jsonData);
                            logger.info("Loaded test data from: {}", file.getName());
                        } catch (IOException e) {
                            logger.error("Error loading JSON test data from {}: {}", file.getName(), e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error loading JSON test data: {}", e.getMessage());
        }
    }

    /**
     * Load environment variables as test data
     */
    private void loadEnvironmentVariables() {
        Map<String, String> envVars = new HashMap<>();
        for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
            if (entry.getKey().startsWith("TEST_")) {
                envVars.put(entry.getKey(), entry.getValue());
            }
        }
        if (!envVars.isEmpty()) {
            testData.put("environment", envVars);
            logger.info("Loaded {} environment variables as test data", envVars.size());
        }
    }

    /**
     * Get test data by key
     */
    public Object getTestData(String key) {
        return testData.get(key);
    }

    /**
     * Get test data as string
     */
    public String getTestDataAsString(String key) {
        Object data = testData.get(key);
        return data != null ? data.toString() : null;
    }

    /**
     * Get test data as map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getTestDataAsMap(String key) {
        Object data = testData.get(key);
        if (data instanceof Map) {
            return (Map<String, Object>) data;
        }
        return null;
    }

    /**
     * Get properties test data
     */
    public Properties getPropertiesTestData(String fileName) {
        Object data = testData.get(fileName);
        if (data instanceof Properties) {
            return (Properties) data;
        }
        return null;
    }

    /**
     * Get JSON test data
     */
    public Object getJsonTestData(String fileName) {
        return testData.get(fileName);
    }

    /**
     * Get environment variable
     */
    public String getEnvironmentVariable(String key) {
        @SuppressWarnings("unchecked")
        Map<String, String> envVars = (Map<String, String>) testData.get("environment");
        if (envVars != null) {
            return envVars.get(key);
        }
        return null;
    }

    /**
     * Add test data dynamically
     */
    public void addTestData(String key, Object value) {
        testData.put(key, value);
        logger.info("Added test data: {} = {}", key, value);
    }

    /**
     * Remove test data
     */
    public void removeTestData(String key) {
        testData.remove(key);
        logger.info("Removed test data: {}", key);
    }

    /**
     * Clear all test data
     */
    public void clearTestData() {
        testData.clear();
        logger.info("Cleared all test data");
    }

    /**
     * Get all test data keys
     */
    public String[] getAllTestDataKeys() {
        return testData.keySet().toArray(new String[0]);
    }

    /**
     * Check if test data exists
     */
    public boolean hasTestData(String key) {
        return testData.containsKey(key);
    }

    /**
     * Get test data with default value
     */
    public Object getTestDataWithDefault(String key, Object defaultValue) {
        return testData.getOrDefault(key, defaultValue);
    }

    /**
     * Get test data as string with default value
     */
    public String getTestDataAsStringWithDefault(String key, String defaultValue) {
        Object data = testData.get(key);
        return data != null ? data.toString() : defaultValue;
    }

    /**
     * Reload test data
     */
    public void reloadTestData() {
        testData.clear();
        loadTestData();
        logger.info("Test data reloaded");
    }
} 