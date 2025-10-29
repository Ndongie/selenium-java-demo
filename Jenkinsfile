pipeline {
    agent any

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

        stage('Build') {
            steps {
                sh 'mvn clean compile -q'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Set the URL based on the environment parameter
                    def url = ""
                    if (params.environment == "staging") {
                        url = "https://practice.qabrains.com/ecommerce/"
                    } else if (params.environment == "preprod") {
                        url = "https://practice.qabrains.com/ecommerce/"
                    } else if (params.environment == "prod") {
                        url = "https://practice.qabrains.com/ecommerce/"
                    }

                    // Build test command
                    def testCommand = "mvn test -Durl=${url} -Dbrowser=${params.BROWSER}"

                    // Add test suite parameter if not 'all'
                    if (params.TEST_SUITE != 'all') {
                        testCommand += " -P${params.TEST_SUITE}"
                    }

                    if (params.RUN_PARALLEL) {
                        testCommand += " -Dparallel=true"
                    }
                    if (params.HEADLESS) {
                        testCommand += " -Dheadless=true"
                    }

                     echo "Executing command: ${testCommand}"
                     if (isUnix()) {
                        sh testCommand
                     } else {
                        bat testCommand
                     }
                }
            }
            post {
                always {
                    // Archive JUnit test results
                    junit 'target/surefire-reports/**/*.xml'

                    // Publish HTML reports
                    publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'target/surefire-reports',
                        reportFiles: 'extent-report.html',
                        reportName: 'TestNG Report'
                    ])

                    // Generate Allure report
                    sh 'mvn allure:report'

                    // Archive test results for Allure
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            // Send notifications
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

            // Clean workspace at the very end
            cleanWs()
        }
        success {
            echo 'Tests executed successfully!'
            echo "Allure Report: ${env.BUILD_URL}allure/"
        }
        failure {
            echo 'Tests failed! Check the reports for details.'
            echo "Allure Report: ${env.BUILD_URL}allure/"
        }
    }
}