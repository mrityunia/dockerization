# Automation Test Framework

A comprehensive test automation framework built with **Selenium WebDriver**, **REST Assured**, **Cucumber BDD**, **Java**, **Log4j**, and **Maven**. This framework supports both UI and API testing with parallel execution capabilities.

## 🚀 Features

- **Selenium WebDriver**: Cross-browser UI automation
- **REST Assured**: Comprehensive API testing
- **Cucumber BDD**: Behavior-driven development with Gherkin syntax
- **Parallel Execution**: Support for both sequential and parallel test execution
- **Log4j**: Comprehensive logging with multiple appenders
- **Page Object Model**: Maintainable and scalable UI test structure
- **Configuration Management**: Centralized configuration with properties file
- **Screenshot Capture**: Automatic screenshot capture on test failures
- **Test Reports**: Multiple report formats (HTML, JSON, XML)
- **Maven Integration**: Easy dependency management and build process

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Chrome browser (recommended) or Safari browser (for UI tests)

## 🛠️ Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd test-framework
   ```

2. **Install dependencies**
   ```bash
   mvn clean install
   ```

3. **Configure the framework**
   - Edit `src/main/resources/config.properties` to set your application URLs and test data paths
   - Update browser settings in the configuration file

## 🏗️ Project Structure

```
src/
├── main/
│   ├── java/com/automation/
│   │   ├── config/          # Configuration management
│   │   ├── drivers/         # WebDriver management
│   │   ├── pageobjects/     # Page Object classes
│   │   ├── api/            # REST API client
│   │   ├── utils/          # Utility classes
│   │   └── testdata/       # Test data management
│   └── resources/
│       ├── config.properties # Framework configuration
│       └── log4j2.xml      # Logging configuration
└── test/
    ├── java/com/automation/
    │   ├── ui/             # UI test runners
    │   ├── api/            # API test runners
    │   ├── steps/          # Cucumber step definitions
    │   └── hooks/          # Cucumber hooks
    └── resources/
        ├── features/       # Cucumber feature files
        │   ├── ui/         # UI test features
        │   └── api/        # API test features
        ├── config/         # Test-specific configuration
        └── testdata/       # Test data files
```

## 🚀 Running Tests

### All Tests (Sequential)
```bash
mvn test -Psequential
```

### All Tests (Parallel)
```bash
mvn test -Pparallel
```

### UI Tests Only (Chrome - Default)
```bash
mvn test -Pui-tests
```

### Chrome-Specific Tests
```bash
mvn test -Pchrome-tests
```

### UI Tests with Safari
```bash
mvn test -Pui-tests -Dbrowser.name=safari
```

### Safari-Specific Tests
```bash
mvn test -Dtest=SafariTestRunner
```

### API Tests Only
```bash
mvn test -Papi-tests
```

### Specific Tags
```bash
# Run smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Run UI tests with specific tag
mvn test -Dcucumber.filter.tags="@ui and @smoke"

# Run API tests with specific tag
mvn test -Dcucumber.filter.tags="@api and @get"
```

### Custom Configuration
```bash
# Run with specific browser
mvn test -Dbrowser.name=chrome
mvn test -Dbrowser.name=safari

# Run in headless mode
mvn test -Dbrowser.headless=true

# Run with custom thread count
mvn test -Pparallel -Dparallel.thread.count=8
```

## 📝 Writing Tests

### UI Tests

1. **Create Feature File** (`src/test/resources/features/ui/example.feature`)
```gherkin
@ui @example
Feature: Example UI Test
  As a user
  I want to perform actions on a web page
  So that I can verify the functionality

  @smoke
  Scenario: Basic functionality test
    Given I am on the homepage
    When I click on the login button
    Then I should see the login form
```

2. **Create Step Definitions** (`src/test/java/com/automation/steps/ExampleSteps.java`)
```java
package com.automation.steps;

import com.automation.pageobjects.ExamplePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class ExampleSteps {
    private ExamplePage examplePage = new ExamplePage();

    @Given("I am on the homepage")
    public void i_am_on_the_homepage() {
        examplePage.navigateToHomepage();
    }

    @When("I click on the login button")
    public void i_click_on_the_login_button() {
        examplePage.clickLoginButton();
    }

    @Then("I should see the login form")
    public void i_should_see_the_login_form() {
        Assert.assertTrue(examplePage.isLoginFormDisplayed());
    }
}
```

3. **Create Page Object** (`src/main/java/com/automation/pageobjects/ExamplePage.java`)
```java
package com.automation.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;

public class ExamplePage extends BasePage {
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(id = "login-form")
    private WebElement loginForm;

    public void navigateToHomepage() {
        navigateTo("https://example.com");
    }

    public void clickLoginButton() {
        click(By.id("login-button"));
    }

    public boolean isLoginFormDisplayed() {
        return isElementDisplayed(By.id("login-form"));
    }
}
```

### API Tests

1. **Create Feature File** (`src/test/resources/features/api/example_api.feature`)
```gherkin
@api @example
Feature: Example API Test
  As a developer
  I want to test API endpoints
  So that I can verify the API functionality

  @smoke
  Scenario: Get user information
    Given I have access to the API
    When I send a GET request to "/users/1"
    Then the response status code should be 200
    And the response should contain user information
```

2. **Create Step Definitions** (`src/test/java/com/automation/steps/ExampleApiSteps.java`)
```java
package com.automation.steps;

import com.automation.api.RestApiClient;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

public class ExampleApiSteps {
    private RestApiClient apiClient = new RestApiClient();
    private Response response;

    @Given("I have access to the API")
    public void i_have_access_to_the_api() {
        // API client is already initialized
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        response = apiClient.get(endpoint);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the response should contain user information")
    public void the_response_should_contain_user_information() {
        Assert.assertNotNull(response.jsonPath().get("id"));
        Assert.assertNotNull(response.jsonPath().get("name"));
    }
}
```

## ⚙️ Configuration

### Main Configuration (`src/main/resources/config.properties`)
```properties
# Application Configuration
app.base.url=https://www.google.com
app.timeout=30

# Browser Configuration
browser.name=chrome
browser.headless=false
browser.window.size=1920x1080

# API Configuration
api.base.url=https://jsonplaceholder.typicode.com
api.timeout=30

# Parallel Execution Configuration
parallel.thread.count=4
parallel.execution.enabled=true

# Screenshot Configuration
screenshot.on.failure=true
screenshot.format=png
```

### Logging Configuration (`src/main/resources/log4j2.xml`)
The framework includes comprehensive logging with:
- Console output
- File logging
- Rolling file logging
- HTML report logging

## 📊 Test Reports

After test execution, reports are generated in:
- `target/cucumber-reports/` - Cucumber HTML reports
- `target/surefire-reports/` - TestNG reports
- `logs/` - Log files
- `screenshots/` - Screenshots (on failure)

## 🔧 Maven Profiles

The framework includes several Maven profiles for different execution modes:

- **sequential**: Run tests sequentially
- **parallel**: Run tests in parallel
- **ui-tests**: Run only UI tests
- **api-tests**: Run only API tests

## 🐛 Troubleshooting

### Common Issues

1. **WebDriver Issues**
   - Ensure the correct browser is installed (Chrome or Safari)
   - Check WebDriverManager configuration
   - Verify browser version compatibility
   - For Safari: Enable Safari WebDriver in Safari's Develop menu

2. **API Test Issues**
   - Verify API endpoints are accessible
   - Check network connectivity
   - Validate API response format

3. **Parallel Execution Issues**
   - Reduce thread count if tests are failing
   - Check for resource conflicts
   - Verify thread-safe implementation

### Debug Mode
```bash
# Run with debug logging
mvn test -Dlogging.level=DEBUG

# Run with verbose output
mvn test -X
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the example tests

---

**Happy Testing! 🧪** 