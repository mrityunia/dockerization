# Dockerized Automation Framework

A simple and efficient test automation framework using Selenium Grid with official Docker images. This setup uses pre-built official images for maximum simplicity and minimal configuration.

## 🚀 Features

- **Simple Setup**: Uses official pre-built Docker images
- **Selenium Grid**: Distributed testing with Chrome and Firefox nodes
- **Multi-Browser Support**: Chrome and Firefox browsers
- **Unified Test Runner**: Single test runner for all tests (UI + API)
- **Parallel & Sequential Execution**: Configurable thread count for concurrent test sessions
- **API Testing**: REST Assured integration
- **BDD Framework**: Cucumber with Gherkin syntax
- **Parameterized Execution**: Configurable browser, threads, and tags
- **Unified Reporting**: Single comprehensive report for all test types
- **Report Generation**: HTML, JSON, XML, and ExtentReports
- **Base64 Screenshot Capture**: Optimized for database storage with reduced execution size
- **Logging**: Comprehensive logging with Log4j2

## 📋 Prerequisites

- Docker and Docker Compose
- At least 8GB RAM (recommended for parallel execution)
- 4 CPU cores (recommended)

## 🚀 Simple Architecture

### Official Images Used
- **Selenium Hub**: `selenium/hub:4.15.0`
- **Chrome Node**: `selenium/node-chrome:4.15.0`
- **Firefox Node**: `selenium/node-firefox:4.15.0`
- **Maven**: `maven:3.9.6-eclipse-temurin-11`

### Benefits
- ✅ No custom Docker builds required
- ✅ Uses official, maintained images
- ✅ Minimal configuration needed
- ✅ Fast startup and deployment
- ✅ Reliable and stable
- ✅ Easy to understand and maintain

## 🏗️ Project Structure

```
dockerization/
├── .dockerignore             # Docker ignore file
├── .gitignore               # Git ignore file
├── docker-compose.yml       # Multi-service orchestration
├── pom.xml                  # Maven dependencies
├── README.md               # Documentation
└── src/
    ├── main/java/
    │   └── com/automation/
    │       ├── api/         # API client classes
    │       ├── config/      # Configuration management
    │       ├── drivers/     # WebDriver management
    │       ├── pageobjects/ # Page Object Model
    │       ├── testdata/    # Test data management
    │       └── utils/       # Utility classes
    └── test/
        ├── java/
        │   └── com/automation/
        │       ├── hooks/   # Test hooks (UI + API)
        │       ├── steps/   # Cucumber step definitions
        │       └── UnifiedTestRunner.java  # Single test runner for all tests
        └── resources/
            ├── config/      # Test configuration
            ├── features/    # Cucumber feature files
            │   ├── api/     # API test features
            │   └── ui/      # UI test features
            └── testdata/    # Test data files
```

## 🐳 Quick Start

### 1. Start Selenium Grid

```bash
# Start Selenium Grid with Chrome and Firefox nodes
docker-compose up -d selenium-hub chrome firefox

# Check if grid is ready
curl http://localhost:4444/wd/hub/status
```

### 2. Run Unified Tests

```bash
# Run all tests (UI + API) in parallel
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub

# Run all tests sequentially
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.execution=none

# Run with specific tags
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@smoke"

# Run with custom thread count
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.thread.count=3

# Run Firefox tests
docker-compose run --rm test-runner mvn clean test -Dbrowser=firefox -DenvUrl=http://selenium-hub:4444/wd/hub
```

### 3. Stop Services

```bash
# Stop all services
docker-compose down
```

### 3. Run with Custom Parameters

```bash
# Run with custom tags and browser
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@smoke"

# Run with custom thread count
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.thread.count=4

# Run API tests with specific tags
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@api and @critical"

# Run sequentially with specific browser
docker-compose run --rm test-runner mvn clean test -Dbrowser=firefox -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.execution=none
```

## ⚙️ Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `BROWSER` | `chrome` | Browser to use (chrome, firefox) |
| `THREADS` | `2` | Number of parallel threads |
| `TAGS` | `@ui` | Cucumber tags to filter tests |
| `GRID_URL` | `http://selenium-hub:4444/wd/hub` | Selenium Grid URL |

### Service Configuration

#### Selenium Grid Services
- **selenium-hub**: Port 4444, Grid hub for managing browser nodes
- **chrome**: Chrome browser node with 4 max sessions
- **firefox**: Firefox browser node with 4 max sessions
- **test-runner**: Maven container for running tests

## 📊 Unified Reporting

The framework generates a single comprehensive report for all tests (UI + API):

### Report Types
- **ExtentReports**: `target/extent-reports/extent-report.html` (Enhanced HTML report)
- **Cucumber HTML**: `target/cucumber-reports/unified-test-report.html` (Standard Cucumber report)
- **Cucumber JSON**: `target/cucumber-reports/unified-test-report.json` (Machine-readable format)

### Report Features
- ✅ Single report for all test types (UI + API)
- ✅ Parallel execution tracking
- ✅ Base64 screenshot capture for failed UI tests
- ✅ Detailed test execution logs
- ✅ Browser and environment information
- ✅ Test categorization by tags

## 📸 Base64 Screenshot Capture

The framework captures screenshots as base64 encoded strings, optimized for database storage:

### Screenshot Features
- ✅ **Base64 Encoding**: Screenshots stored as encoded strings
- ✅ **Database Ready**: Perfect for storing in database fields
- ✅ **Reduced Size**: Smaller execution footprint
- ✅ **Metadata Support**: Includes test name, step name, timestamp
- ✅ **Compression Ready**: Framework for future compression
- ✅ **Easy Retrieval**: Convert back to bytes when needed

### Usage Examples
```java
// Basic base64 screenshot
String base64Screenshot = page.takeScreenshotAsBase64();

// Screenshot with metadata for database
ScreenshotData data = page.takeScreenshotWithMetadata("testName", "stepName");

// Compressed screenshot for smaller storage
String compressedScreenshot = page.takeScreenshotAsCompressedBase64();
```

### Database Storage Benefits
- 🗄️ **Efficient Storage**: Base64 strings in database fields
- 📊 **Easy Querying**: Search and filter by metadata
- 🔄 **Simple Retrieval**: Convert back to images when needed
- 💾 **Reduced File System**: No file system dependencies
- 🚀 **Faster Execution**: No file I/O operations

## 🎯 Usage Examples

### 1. Parallel Execution

```bash
# Run all tests in parallel (default)
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub

# Run with custom thread count
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.thread.count=4
```

### 2. Sequential Execution

```bash
# Run all tests sequentially
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.execution=none

# Run Firefox tests sequentially
docker-compose run --rm test-runner mvn clean test -Dbrowser=firefox -DenvUrl=http://selenium-hub:4444/wd/hub -Dparallel.execution=none
```

### 3. Tag-Based Testing

```bash
# Run smoke tests only
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@smoke"

# Run API tests only
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@api"

# Run UI tests only
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@ui"
```

### 4. Cross-Browser Testing

```bash
# Run same tests on different browsers
docker-compose run --rm test-runner mvn clean test -Dbrowser=chrome -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@ui"
docker-compose run --rm test-runner mvn clean test -Dbrowser=firefox -DenvUrl=http://selenium-hub:4444/wd/hub -Dcucumber.filter.tags="@ui"
```

## 📊 Monitoring and Debugging

### View Test Execution

```bash
# View logs for specific service
docker-compose logs -f chrome-1

# View all logs
docker-compose logs -f

# View logs for multiple services
docker-compose logs -f chrome-1 chrome-2
```

### VNC Access

Access browser sessions via VNC:
- Chrome-1: `localhost:7901`
- Chrome-2: `localhost:7902`
- Chrome-3: `localhost:7903`
- Safari: `localhost:7904`
- API Tests: `localhost:7905`
- Sequential: `localhost:7906`

### Selenium Grid

Access Selenium Grid console:
- Grid Hub: `http://localhost:4444`
- Grid Status: `http://localhost:4444/ui`

## 📁 Output Files

### Reports
- Location: `./reports/`
- Formats: HTML, JSON, XML
- Generated after each test run

### Screenshots
- Location: `./screenshots/`
- Captured on test failures
- Organized by test name and timestamp

### Logs
- Location: `./logs/`
- Detailed execution logs
- Error tracking and debugging

### Downloads
- Location: `./downloads/`
- Files downloaded during tests
- Temporary test data

## 🔧 Advanced Configuration

### Custom Maven Commands

```bash
# Run with custom Maven goals
docker-compose run --rm chrome-1 mvn clean test -Dtest=UITestRunner

# Run with specific profiles
docker-compose run --rm chrome-1 mvn clean test -P smoke

# Run with custom system properties
docker-compose run --rm chrome-1 mvn clean test -Dbrowser.headless=true
```

### Selenium Grid Integration

```bash
# Start Selenium Grid
docker-compose up selenium-hub chrome-node-1 chrome-node-2 safari-node

# Run tests against Grid
docker-compose run --rm test-runner
```

### Custom Docker Commands

```bash
# Execute commands in running container
docker-compose exec chrome-1 bash

# Run single test in container
docker-compose run --rm chrome-1 mvn test -Dtest=GoogleSearchTest

# Debug container
docker-compose run --rm --entrypoint bash chrome-1
```

## 🛠️ Troubleshooting

### Common Issues

1. **Out of Memory**
   ```bash
   # Increase shared memory
   docker-compose up --shm-size=4g chrome-1
   ```

2. **Port Conflicts**
   ```bash
   # Use different ports
   docker-compose -f docker-compose.yml -f docker-compose.override.yml up
   ```

3. **Browser Crashes**
   ```bash
   # Restart specific service
   docker-compose restart chrome-1
   ```

4. **Test Failures**
   ```bash
   # Check logs
   docker-compose logs chrome-1
   
   # View screenshots
   ls -la screenshots/
   ```

### Performance Optimization

```bash
# Run with resource limits
docker-compose up --cpus=2 --memory=4g chrome-1

# Use host networking (Linux only)
docker-compose up --network=host chrome-1
```

## 📈 Scaling

### Horizontal Scaling

```bash
# Scale Chrome instances
docker-compose up --scale chrome-node=5

# Scale with custom configuration
docker-compose -f docker-compose.yml -f docker-compose.scale.yml up
```

### Load Balancing

```bash
# Use Selenium Grid for load balancing
docker-compose up selenium-hub chrome-node-1 chrome-node-2 chrome-node-3
```

## 🔒 Security

### Best Practices

1. **Network Isolation**
   ```yaml
   networks:
     automation-network:
       driver: bridge
       internal: true
   ```

2. **Resource Limits**
   ```yaml
   deploy:
     resources:
       limits:
         cpus: '2'
         memory: 4G
   ```

3. **Non-Root User**
   ```dockerfile
   USER 1000:1000
   ```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the troubleshooting section
- Review the logs and documentation

---

**Happy Testing! 🚀** 