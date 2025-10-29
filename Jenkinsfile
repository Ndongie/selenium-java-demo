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
                    try {
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

                        echo "Executing command: ${testCommand}"

                        if (isUnix()) {
                            sh testCommand
                        } else {
                            bat testCommand
                        }
                    } catch (Exception e) {
                        echo "Test execution failed: ${e.getMessage()}"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }

        stage('Generate Reports') {
            steps {
                script {
                    // Generate Allure report data
                    if (isUnix()) {
                        sh 'mvn allure:report'
                    } else {
                        bat 'mvn allure:report'
                    }
                }
            }
            post {
                always {
                    // Publish Allure report
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])

                    // Archive JUnit results
                    junit 'target/surefire-reports/**/*.xml'

                    // Archive HTML reports if they exist
                    archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**/*, target/surefire-reports/*.html', allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            script {
                // Send notifications only if email is configured
                try {
                    emailext (
                        subject: "Build Result: ${currentBuild.currentResult} - ${env.JOB_NAME}",
                        body: """
                        Build: ${env.BUILD_URL}<br/>
                        Result: ${currentBuild.currentResult}<br/>
                        Test Results: ${env.BUILD_URL}testReport/<br/>
                        Allure Report: ${env.BUILD_URL}allure/<br/>
                        """,
                        to: "ndongieawona@gmail.com"
                    )
                } catch (Exception e) {
                    echo "Failed to send email: ${e.getMessage()}"
                }

                // Clean workspace at the very end (optional - you might want to keep it for debugging)
                // cleanWs()
            }
        }
        success {
            echo 'Tests executed successfully!'
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