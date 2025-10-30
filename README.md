# Selenium Java Test Automation Framework
A comprehensive test automation framework built with Selenium WebDriver, Java, TestNG, and Maven, integrated with Jenkins for continuous testing.

## 📋 Table of Contents
<!-- TOC -->
* [Selenium Java Test Automation Framework](#selenium-java-test-automation-framework)
  * [📋 Table of Contents](#-table-of-contents)
  * [🚀 Overview](#-overview)
  * [✨ Features](#-features)
  * [🛠 Prerequisites](#-prerequisites)
    * [Required Software](#required-software)
    * [Browser Drivers](#browser-drivers)
  * [📁 Project Structure](#-project-structure)
  * [⚙️ Installation & Setup](#-installation--setup)
    * [1. Clone the Repository](#1-clone-the-repository)
    * [2. Verify Java Installation](#2-verify-java-installation)
      * [Should show Java 17 or higher](#should-show-java-17-or-higher)
    * [3. Verify Maven Installation](#3-verify-maven-installation)
    * [4. Install Dependencies](#4-install-dependencies)
  * [🧪 Running Tests](#-running-tests)
    * [Running All Tests](#running-all-tests)
    * [Running Specific Test Suites](#running-specific-test-suites)
      * [Smoke tests](#smoke-tests)
      * [Product-related tests](#product-related-tests)
      * [Sorting tests](#sorting-tests)
      * [Authentication tests](#authentication-tests)
    * [Running with Custom Parameters](#running-with-custom-parameters)
      * [Specific browser](#specific-browser)
      * [Headless mode](#headless-mode)
      * [Custom URL](#custom-url)
      * [Parallel execution](#parallel-execution)
  * [⚙️ Test Configuration](#-test-configuration)
    * [Environment Variables](#environment-variables)
    * [TestNG Suite Configuration](#testng-suite-configuration)
  * [🚀 Jenkins Integration](#-jenkins-integration)
    * [Pipeline Features](#pipeline-features)
    * [Jenkins Setup](#jenkins-setup)
    * [Jenkins Build Parameters](#jenkins-build-parameters)
  * [📊 Reporting](#-reporting)
    * [Allure Reports](#allure-reports)
    * [View Reports Locally](#view-reports-locally)
    * [Generate and open Allure report](#generate-and-open-allure-report)
    * [Generate Static Report](#generate-static-report)
    * [Report available at: target/site/allure-maven-plugin/index.html](#report-available-at-targetsiteallure-maven-pluginindexhtml)
      * [Accessing Jenkins Reports](#accessing-jenkins-reports)
  * [🧩 Test Suites](#-test-suites)
    * [Available Test Suites](#available-test-suites)
  * [🌐 Browser Support](#-browser-support)
  * [🔧 Troubleshooting](#-troubleshooting)
    * [Common Issues](#common-issues)
    * [Logs and Debugging](#logs-and-debugging)
    * [📞 Support](#-support)
    * [Check existing Allure reports for test failures](#check-existing-allure-reports-for-test-failures)
    * [Review Jenkins build logs](#review-jenkins-build-logs)
    * [Verify environment configuration](#verify-environment-configuration)
    * [Ensure all prerequisites are met](#ensure-all-prerequisites-are-met)
<!-- TOC -->


## 🚀 Overview

This framework provides a robust solution for automated web testing with the following key components:
- **Selenium WebDriver 4.33.0** for browser automation
- **TestNG 7.10.2** as the testing framework
- **Maven** for dependency management and build automation
- **Allure 2.27.0** for comprehensive test reporting
- **Jenkins** for CI/CD pipeline integration

## ✨ Features

- ✅ Multi-browser support (Chrome, Firefox, Edge, Safari)
- ✅ Parallel test execution capability
- ✅ Multiple test suite configurations
- ✅ Comprehensive reporting with Allure
- ✅ Headless browser execution
- ✅ Environment-specific testing
- ✅ Jenkins pipeline with parameterized builds
- ✅ ExtentReports integration
- ✅ JSON data processing with Jackson
- ✅ Cross-platform compatibility (Windows/Linux/macOS)

## 🛠 Prerequisites

### Required Software
- **Java 17** or higher
- **Maven 3.6+**
- **Git**
- **Allure Commandline** (for local report generation)

### Browser Drivers
The framework automatically manages browser drivers through Selenium Manager. Ensure you have the following browsers installed:
- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Safari (macOS only)

## 📁 Project Structure
selenium-java-demo/
├── src/
│ ├── main/java/ # Page Objects, Utilities, Helpers
│ └── test/
│ ├── java/ # Test classes
│ └── testng/ # TestNG suite configurations
│ ├── alltests.xml
│ ├── regression.xml
│ ├── smoke.xml
│ ├── product.xml
│ ├── sorting.xml
│ └── authentication.xml
├── target/
│ ├── allure-results/ # Allure test results
│ └── site/allure-maven-plugin/ # Generated reports
├── pom.xml # Maven configuration
└── Jenkinsfile # Jenkins pipeline definition



## ⚙️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Ndongie/selenium-java-demo.git
cd selenium-java-demo
```
### 2. Verify Java Installation
```bash
java -version
```
#### Should show Java 17 or higher

### 3. Verify Maven Installation
```bash
mvn -version
```

### 4. Install Dependencies
```bash
mvn clean install
```


## 🧪 Running Tests
### Running All Tests
```bash
mvn test -Pall
```

### Running Specific Test Suites
```bash
#### Regression tests
mvn test -Pregression
```

#### Smoke tests
```
mvn test -Psmoke
```

#### Product-related tests
```
mvn test -Pproduct
```

#### Sorting tests
```
mvn test -Psorting
```

#### Authentication tests
```
mvn test -Pauthentication
```

### Running with Custom Parameters
#### Specific browser
```
mvn test -Pregression -Dbrowser=firefox
```

#### Headless mode
```
mvn test -Psmoke -Dheadless=true
```

#### Custom URL
```
mvn test -Pall -Durl=https://your-test-environment.com
```

#### Parallel execution
```
mvn test -Pall -Dparallel=true
```

## ⚙️ Test Configuration

### Environment Variables
* browser: Browser to use (chrome, firefox, edge, safari)
* url: Application URL to test 
* headless: Run in headless mode (true/false)
* parallel: Enable parallel execution (true/false)

### TestNG Suite Configuration
**Test suites are defined in src/test/testng/ directory:**
* alltests.xml: Complete test suite 
* regression.xml: Regression test suite 
* smoke.xml: Smoke test suite 
* product.xml: Product-related tests 
* sorting.xml: Sorting functionality tests 
* authentication.xml: Authentication tests

## 🚀 Jenkins Integration
### Pipeline Features
* Parameterized builds with customizable options 
* Automatic workspace cleanup 
* Multi-browser testing 
* Parallel execution support 
* Allure report generation 
* Build status notifications

### Jenkins Setup
**1.Install Required Plugins:**
* Allure Jenkins Plugin 
* Pipeline Plugin 
* Git Plugin

**2.Configure Allure in Jenkins:**
* Go to Manage Jenkins → Global Tool Configuration 
* Add Allure Commandline installation 
* Name: allure 
* Install automatically

**3.Create Pipeline Job:**
* New Item → Pipeline 
* Definition: Pipeline script from SCM 
* SCM: Git 
* Repository URL: https://github.com/Ndongie/selenium-java-demo.git
* Script Path: Jenkinsfile

### Jenkins Build Parameters
| Parameter    | Options                                                  | Description               |
|--------------|----------------------------------------------------------|---------------------------|
| ENVIRONMENT  | staging, preprod, prod                                   | Target environment        |
| BROWSER      | firefox, edge, safari                                    | Browser for testing       |
| TEST_SUITE   | all, regression, sorting, smoke, product, authentication | Test suite to execute     |
| RUN_PARALLEL | true/false                                               | Enable parallel execution |
| HEADLESS     | true/false                                               | Headless browser mode     |


## 📊 Reporting
### Allure Reports
**The framework generates comprehensive Allure reports with:**
* Test execution trends 
* Detailed test steps 
* Screenshots on failure 
* Environment information 
* Test categorization

### View Reports Locally
### Generate and open Allure report
```bash
mvn allure:serve
```
### Generate Static Report
```bash
mvn allure:report
```
### Report available at: target/site/allure-maven-plugin/index.html
#### Accessing Jenkins Reports
**After Jenkins build completion, access reports at:**
${JENKINS_URL}/job/${JOB_NAME}/${BUILD_NUMBER}/allure/

## 🧩 Test Suites
### Available Test Suites
| Suite              | Description                       | Use Case           |
|:-------------------|-----------------------------------|--------------------|
| **smoke**          | Critical path tests               | Build verification |
| **regression**     | Comprehensive functionality tests | Release validation |
| **authentication** | Login/logout tests                | Security testing   |
| **product**        | Product management tests          | Feature testing    |
| **sorting**        | Sorting functionality tests       | UI validation      |
| **all**            | Complete test coverage            | Full regression    |


## 🌐 Browser Support
| Browser              | Version | Support Level | Headless | Parallel | Mobile Emulation | Notes                               |
|----------------------|:-------:|:-------------:|:--------:|:--------:|:----------------:|-------------------------------------|
| **Chrome**           |   90+   |    ✅ Full     |  ✅ Yes   |  ✅ Yes   |      ✅ Yes       | Primary supported browser           |
| **Firefox**          |   90+   |    ✅ Full     |  ✅ Yes   |  ✅ Yes   |    ⚠️ Limited    | Secondary supported browser         |
| **Edge**             |   90+   |    ✅ Full     |  ✅ Yes   |  ✅ Yes   |      ✅ Yes       | Chromium-based, Windows recommended |
| **Safari**           |   14+   |  ⚠️ Limited   |   ❌ No   | ⚠️ Basic |       ❌ No       | macOS only, manual driver setup     |
| **Chrome Headless**  |   90+   |    ✅ Full     | ✅ Always |  ✅ Yes   |      ✅ Yes       | CI/CD recommended                   |
| **Firefox Headless** |   90+   |    ✅ Full     | ✅ Always |  ✅ Yes   |    ⚠️ Limited    | CI/CD alternative                   |


## 🔧 Troubleshooting
### Common Issues
1. Browser Driver Issues
* Ensure browsers are installed and updated 
* Framework uses Selenium Manager for automatic driver management 

2. Allure Report Generation
* Verify Allure is installed in Jenkins 
* Check target/allure-results directory exists after test execution

3. Parallel Execution
* Ensure tests are thread-safe
* Use ThreadLocal for WebDriver instances

4. Headless Mode
* Some visual validations may fail in headless mode 
* Use -Dheadless=false to debug

### Logs and Debugging
* Test execution logs are available in Jenkins console output 
* Allure reports include detailed step-by-step execution 
* Use -Dmaven.test.failure.ignore=true to continue build on test failures

### 📞 Support
* For issues and questions: ndongieawona@gmail.com

### Check existing Allure reports for test failures
### Review Jenkins build logs
### Verify environment configuration
### Ensure all prerequisites are met

<br>
Framework Version: 1.0
Maintainer: Ndongie Awona
Last Updated: 30/10/2025