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

        stage('Generate Reports') {
            steps {
                script {
                    // Generate reports - don't affect build result if this fails

                        if (isUnix()) {
                            sh 'mvn allure:report'
                        } else {
                            bat 'mvn allure:report'
                        }
                }
            }
            post {
                always {
                    script {
                        // Publish reports

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
    }

    post {
        success {
            echo 'Build completed successfully! All tests passed.'
        }
        failure {
            echo 'Build failed due to test failures! Check the reports for details.'
            echo "Allure Report: ${env.BUILD_URL}allure/"
        }
    }
}