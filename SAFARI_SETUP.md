# Safari Browser Setup Guide

This guide will help you set up Safari browser for the automation framework.

## 🍎 Safari Requirements

- **macOS**: Safari automation only works on macOS
- **Safari Version**: Safari 12 or higher
- **Safari WebDriver**: Must be enabled in Safari

## 🔧 Safari WebDriver Setup

### Step 1: Enable Safari WebDriver

1. **Open Safari**
2. **Enable Develop Menu**:
   - Go to `Safari` → `Preferences` → `Advanced`
   - Check "Show Develop menu in menu bar"

3. **Enable WebDriver**:
   - Go to `Develop` → `Allow Remote Automation`
   - This will enable Safari WebDriver

### Step 2: Verify Safari WebDriver

1. **Check Safari WebDriver Status**:
   ```bash
   # Check if Safari WebDriver is running
   curl http://localhost:4444/status
   ```

2. **If not running, start Safari WebDriver**:
   ```bash
   # Start Safari WebDriver manually (if needed)
   safaridriver --enable
   ```

### Step 3: Test Safari WebDriver

Run a simple test to verify Safari WebDriver is working:

```bash
# Test Safari WebDriver
mvn test -Dcucumber.filter.tags="@ui and @smoke" -Dbrowser.name=safari
```

## ⚙️ Safari-Specific Configuration

### Configuration Properties

The framework is configured to use Safari by default:

```properties
# Browser Configuration
browser.name=safari
browser.headless=false
browser.window.size=1920x1080
```

### Safari Capabilities

The framework includes Safari-specific capabilities:

```java
SafariOptions options = new SafariOptions();
options.setCapability("safari.cleanSession", true);
options.setCapability("safari.useSimulator", false);
```

## 🚀 Running Tests with Safari

### Basic Commands

```bash
# Run all UI tests with Safari
mvn test -Pui-tests

# Run specific UI tests
mvn test -Dcucumber.filter.tags="@ui and @google"

# Run tests in parallel with Safari
mvn test -Pparallel
```

### Safari-Specific Options

```bash
# Run with specific Safari window size
mvn test -Dbrowser.window.size=1366x768

# Run with Safari debugging (if needed)
mvn test -Dsafari.debug=true
```

## 🔍 Troubleshooting Safari Issues

### Common Safari WebDriver Issues

1. **"Safari WebDriver not found"**
   - Ensure Safari WebDriver is enabled in Develop menu
   - Restart Safari after enabling WebDriver
   - Check if safaridriver is in your PATH

2. **"Permission denied"**
   - Grant necessary permissions to Safari
   - Check System Preferences → Security & Privacy
   - Allow Safari to control other applications

3. **"Element not found"**
   - Safari may have different element selectors
   - Use Safari's Web Inspector to verify selectors
   - Consider Safari-specific wait strategies

4. **"Session not created"**
   - Close all Safari windows before running tests
   - Restart Safari WebDriver
   - Check Safari version compatibility

### Safari Debugging

1. **Enable Safari Debugging**:
   ```bash
   # Start Safari with debugging
   safaridriver --enable --debug
   ```

2. **Check Safari Logs**:
   ```bash
   # View Safari WebDriver logs
   tail -f /var/log/system.log | grep safaridriver
   ```

3. **Safari Web Inspector**:
   - Use Safari's Web Inspector for debugging
   - Right-click → Inspect Element
   - Check Console for JavaScript errors

## 📱 Safari Mobile Testing (Optional)

For mobile Safari testing:

```java
// Mobile Safari capabilities
SafariOptions options = new SafariOptions();
options.setCapability("platformName", "iOS");
options.setCapability("platformVersion", "15.0");
options.setCapability("deviceName", "iPhone 13");
```

## 🔒 Safari Security Considerations

1. **Automation Security**:
   - Safari may block certain automation actions
   - Grant necessary permissions when prompted
   - Some features may require manual intervention

2. **Privacy Settings**:
   - Check Safari's privacy settings
   - Disable pop-up blockers for test sites
   - Allow cookies for test applications

## 📊 Safari Performance Tips

1. **Optimize Test Performance**:
   - Use Safari's hardware acceleration
   - Minimize browser extensions
   - Close unnecessary Safari tabs

2. **Memory Management**:
   - Safari may use more memory than other browsers
   - Monitor memory usage during test execution
   - Restart Safari between test suites if needed

## ✅ Verification Checklist

- [ ] Safari browser is installed and updated
- [ ] Develop menu is enabled in Safari
- [ ] "Allow Remote Automation" is checked
- [ ] Safari WebDriver is running
- [ ] Framework configuration uses Safari
- [ ] Test execution works with Safari
- [ ] Screenshots are captured correctly
- [ ] Logs are generated properly

## 🆘 Getting Help

If you encounter issues with Safari:

1. **Check Safari WebDriver Status**:
   ```bash
   safaridriver --version
   ```

2. **Verify Framework Configuration**:
   - Check `config.properties` for Safari settings
   - Ensure `browser.name=safari`

3. **Review Logs**:
   - Check `logs/automation.log` for detailed information
   - Look for Safari-specific error messages

4. **Test with Simple Scenario**:
   ```bash
   mvn test -Dcucumber.filter.tags="@smoke" -Dbrowser.name=safari
   ```

---

**Note**: Safari automation is only available on macOS. If you need cross-platform testing, consider using other browsers or cloud-based solutions. 