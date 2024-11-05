pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://192.168.101.4:9000'
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling the repository from GitHub'
                git(branch: 'molkakbaier_5ARCTIC5_G3', url: 'https://github.com/Molka-Kbaier/5arctic5-G3-StationSki.git')
                echo 'Repository pulled successfully!'
            }
        }

        stage('Build with Maven') {
            steps {
                echo 'Starting Maven compile...'
                sh 'mvn clean compile'
                echo 'Maven compile completed!'
            }
        }

        stage('Run Tests and Generate JaCoCo Report') {
            steps {
                echo 'Running tests and generating JaCoCo report...'
                sh 'mvn clean test jacoco:report'
                echo 'JaCoCo report generated successfully!'
            }
        }

        stage('Verify JaCoCo Report') {
            steps {
                echo 'Verifying JaCoCo XML report file...'
                sh 'ls -l target/site/jacoco/jacoco.xml || echo "JaCoCo report not found!"'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withCredentials([string(credentialsId: 'SONAR_TOKEN', variable: 'SONAR_TOKEN')]) {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.host.url=${SONARQUBE_URL} \
                    -Dsonar.login=${SONAR_TOKEN} \
                    -Dsonar.projectKey=Stationski-MolkaKbaier-G3-5ARCTIC5 \
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.junit.reportPaths=target/surefire-reports
                    """
                }
                echo 'SonarQube analysis completed!'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs for details.'
        }
    }
}
