pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://192.168.101.4:9000'
        NEXUS_URL = 'http://192.168.101.4:8081'
        SONARQUBE_CREDENTIALS = credentials('sonarqube-credentials')
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
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

        stage('MVN SONARQUBE') {
            steps {
                echo 'Running SonarQube analysis...'
                sh """
                   mvn sonar:sonar \
                   -Dsonar.login=${SONARQUBE_CREDENTIALS_USR} \
                   -Dsonar.password=${SONARQUBE_CREDENTIALS_PSW}
                """
                echo 'SonarQube analysis completed!'
            }
        }

        stage('MVN MOCKITO') {
            steps {
                echo 'Running Mockito tests...'
                sh 'mvn test'
                echo 'Mockito tests completed!'
            }
        }

        stage('MVN NEXUS') {
            steps {
                echo 'Deploying artifacts to Nexus...'
                sh """
                   mvn deploy -DskipTests \
                   -Dnexus.username=${NEXUS_CREDENTIALS_USR} \
                   -Dnexus.password=${NEXUS_CREDENTIALS_PSW}
                """
                echo 'Artifacts deployed to Nexus successfully!'
            }
        }

        stage('Docker Build') {
            steps {
                echo "======== Building Docker Image ========"
                sh "docker build -t molkak/station-ski:1.0.0 ."
            }
        }

        stage('Docker Push') {
            steps {
                echo "======== Pushing Docker Image to Docker Hub ========"
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    sh 'docker push molkak/station-ski:1.0.0'
                }
            }
        }

        stage('Cleanup Backend Container') {
            steps {
                echo "======== Stopping Existing Backend Container (if any) ========"
                script {
                    def backendContainer = sh(
                        script: "docker ps -q --filter name=totpipline_backend_1",
                        returnStdout: true
                    ).trim()
                    if (backendContainer) {
                        sh "docker stop $backendContainer && docker rm $backendContainer"
                    }
                }
            }
        }

        stage('Cleanup MySQL Container and Image') {
            steps {
                echo "======== Removing MySQL Container and Image (if exists) ========"
                script {
                    def mysqlRunning = sh(script: "docker ps -q --filter ancestor=mysql:8", returnStdout: true).trim()
                    if (mysqlRunning) {
                        sh "docker stop $mysqlRunning && docker rm $mysqlRunning"
                    }
                    def mysqlImageExists = sh(script: "docker images -q mysql:8", returnStdout: true).trim()
                    if (mysqlImageExists) {
                        sh "docker rmi -f mysql:8"
                    }
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "======== Deploying Application with Docker Compose ========"
                sh "docker-compose down || true"
                sh "docker-compose up -d"
            }
        }
    }

    post {
        always {
            echo "======== Cleaning up Docker Resources ========"
            sh "docker system prune -f || true"
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for more details.'
        }
    }
}
