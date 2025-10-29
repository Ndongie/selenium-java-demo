pipeline {
    agent any

    tools{
        allure 'allure'
    }

    parameters {
        choice(
            name: 'ENVIRONMENT',
            choices: ['staging', 'preprod', 'prod'],
            description: 'Select the environment to run tests'
        )
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge', 'safari'],
            description: 'Select the browser in which to run tests'
        )
        choice(
            name: 'TEST_SUITE',
            choices: ['all', 'regression', 'sorting', 'smoke', 'product', 'authentication'],
            description: 'Select the test suite to run'
        )
        booleanParam(
            name: 'RUN_PARALLEL',
            defaultValue: false,
            description: 'Run tests in parallel'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run tests in headless browser'
        )
    }

    stages {
        stage('Clean Workspace') {
            steps {
                script {
                    // Clean previous Allure results
                    if (isUnix()) {
                        sh '''
                            echo "=== Cleaning previous Allure results ==="
                            rm -rf target/allure-results || true
                            rm -rf target/allure-report || true
                            mkdir -p target/allure-results
                        '''
                    } else {
                        bat '''
                            echo "=== Cleaning previous Allure results ==="
                            rmdir /s /q target\\allure-results 2>nul || echo No previous results
                            rmdir /s /q target\\allure-report 2>nul || echo No previous report
                            mkdir target\\allure-results
                        '''
                    }
                }
            }
        }

        stage('Checkout') {
            steps {
                retry(3) {
                    timeout(time: 2, unit: 'MINUTES') {
                        git branch: 'master',
                            url: 'https://github.com/Ndongie/selenium-java-demo.git'
                    }
                }
            }
        }

        stage('Validate Environment') {
            steps {
                script {
                    echo "Build Parameters:"
                    echo "ENVIRONMENT: ${params.ENVIRONMENT}"
                    echo "BROWSER: ${params.BROWSER}"
                    echo "TEST_SUITE: ${params.TEST_SUITE}"
                    echo "RUN_PARALLEL: ${params.RUN_PARALLEL}"
                    echo "HEADLESS: ${params.HEADLESS}"

                    // Validate tools are available
                    if (isUnix()) {
                        sh 'mvn --version'
                        sh 'allure --version'
                    } else {
                        bat 'mvn --version'
                        bat 'allure --version'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    try {
                        if (isUnix()) {
                            sh 'mvn clean compile -q'
                        } else {
                            bat 'mvn clean compile -q'
                        }
                    } catch (Exception e) {
                        error "Build failed: ${e.getMessage()}"
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    def url = "https://practice.qabrains.com/ecommerce/"

                    // Build test command with proper escaping
                    def testCommand = "mvn test -Durl=${url} -Dbrowser=${params.BROWSER}"

                    // Add test suite parameter if not 'all'
                    if (params.TEST_SUITE != 'all') {
                        testCommand += " -P${params.TEST_SUITE}"
                    }

                    if (params.RUN_PARALLEL.toBoolean()) {
                        testCommand += " -Dparallel=true"
                    }
                    if (params.HEADLESS.toBoolean()) {
                        testCommand += " -Dheadless=true"
                    }

                    // Allow Maven to continue even if tests fail
                    testCommand += " -Dmaven.test.failure.ignore=true"

                    echo "Executing command: ${testCommand}"

                    // Execute tests - don't fail the build for test failures
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        if (isUnix()) {
                            sh testCommand
                        } else {
                            bat testCommand
                        }
                    }
                }
            }
        }

        stage('Evaluate Test Results') {
            steps {
                script {
                    // Check if test results exist
                    if (fileExists('target/surefire-reports')) {
                        echo "Test results directory exists"

                        // Use JUnit results to determine if tests failed
                        junit (
                            testResults: 'target/surefire-reports/**/*.xml',
                            allowEmptyResults: true,
                            skipPublishingChecks: true
                        )

                        // Manually check for test failures
                        def testResult = currentBuild.rawBuild.getAction(hudson.tasks.junit.TestResultAction)
                        if (testResult) {
                            def failCount = testResult.failCount
                            def totalCount = testResult.totalCount

                            echo "Test Results Summary:"
                            echo "Total Tests: ${totalCount}"
                            echo "Failed Tests: ${failCount}"
                            echo "Passed Tests: ${totalCount - failCount}"

                            if (failCount > 0) {
                                // This is the ONLY place where we fail the build for tests
                                currentBuild.result = 'FAILURE'
                                error "Build failed due to ${failCount} test failure(s)"
                            } else if (totalCount == 0) {
                                echo "Warning: No tests were executed"
                            } else {
                                echo "All tests passed!"
                                // Explicitly set build to SUCCESS if tests passed
                                currentBuild.result = 'SUCCESS'
                            }
                        } else {
                            echo "No test results found - assuming tests passed or were not executed"
                        }
                    } else {
                        echo "No test results directory found - tests may not have executed"
                    }
                }
            }
        }

        stage('Generate Reports') {
            steps {
                script {
                    // Generate reports - don't affect build result if this fails
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        if (isUnix()) {
                            sh 'mvn allure:report || echo "Allure report generation failed but continuing"'
                        } else {
                            bat 'mvn allure:report || echo "Allure report generation failed but continuing"'
                        }
                    }
                }
            }
            post {
                always {
                    script {
                        // Publish reports - don't affect build result if these fail
                        catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                            allure([
                                includeProperties: false,
                                jdk: '',
                                properties: [],
                                reportBuildPolicy: 'ALWAYS',
                                results: [[path: 'target/allure-results']]
                            ])
                        }

                        catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                            archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**/*, target/surefire-reports/*.html', allowEmptyArchive: true
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                // Send email - completely ignore any failures (don't affect stage or build)
                try {
                    def testResult = currentBuild.rawBuild.getAction(hudson.tasks.junit.TestResultAction)
                    def totalTests = testResult?.totalCount ?: 0
                    def failedTests = testResult?.failCount ?: 0
                    def passedTests = totalTests - failedTests

                    emailext (
                        subject: "Build Result: ${currentBuild.currentResult} - ${env.JOB_NAME}",
                        body: """
                        <html>
                        <body>
                        <h2>Build Notification</h2>
                        <p><strong>Project:</strong> ${env.JOB_NAME}</p>
                        <p><strong>Build Number:</strong> #${env.BUILD_NUMBER}</p>
                        <p><strong>Result:</strong> ${currentBuild.currentResult}</p>
                        <p><strong>Duration:</strong> ${currentBuild.durationString.replace(' and counting', '')}</p>

                        <h3>Test Summary:</h3>
                        <ul>
                            <li>Total Tests: ${totalTests}</li>
                            <li>Passed Tests: ${passedTests}</li>
                            <li>Failed Tests: ${failedTests}</li>
                        </ul>

                        <h3>Links:</h3>
                        <ul>
                            <li><a href="${env.BUILD_URL}">Build Details</a></li>
                            <li><a href="${env.BUILD_URL}testReport/">Test Results</a></li>
                            <li><a href="${env.BUILD_URL}allure/">Allure Report</a></li>
                            <li><a href="${env.BUILD_URL}console">Console Output</a></li>
                        </ul>

                        <h3>Parameters:</h3>
                        <ul>
                            <li>Environment: ${params.ENVIRONMENT}</li>
                            <li>Browser: ${params.BROWSER}</li>
                            <li>Test Suite: ${params.TEST_SUITE}</li>
                            <li>Parallel: ${params.RUN_PARALLEL}</li>
                            <li>Headless: ${params.HEADLESS}</li>
                        </ul>
                        </body>
                        </html>
                        """,
                        to: "ndongieawona@gmail.com",
                        mimeType: "text/html"
                    )
                } catch (Exception e) {
                    // Completely ignore email failures - don't log as error, don't affect build
                    echo "Email notification failed but build continues: ${e.getMessage()}"
                }
            }
        }
        success {
            echo 'Build completed successfully! All tests passed.'
            echo "Allure Report: ${env.BUILD_URL}allure/"
        }
        failure {
            script {
                // Only show failure message if it's due to tests
                def testResult = currentBuild.rawBuild.getAction(hudson.tasks.junit.TestResultAction)
                if (testResult && testResult.failCount > 0) {
                    echo 'Build failed due to test failures! Check the reports for details.'
                } else {
                    echo 'Build failed due to compilation or infrastructure issues!'
                }
            }
            echo "Allure Report: ${env.BUILD_URL}allure/"
        }
    }
}