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
                        script: "docker ps -q --filter name=totpiplineemail_backend_1",
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
            emailext(
                to: 'molka.kbaier@esprit.tn',
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Le pipeline a été exécuté avec succès pour le job ${env.JOB_NAME} - build #${env.BUILD_NUMBER}.",
                mimeType: 'text/html',
                attachLog: true,
                from: 'molka.kbaier@esprit.tn',
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
            )
        }
        failure {
            echo 'Pipeline failed. Please check the logs for more details.'
            emailext(
                to: 'molka.kbaier@esprit.tn',
                subject: "Build Failure: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Le pipeline a échoué pour le job ${env.JOB_NAME} - build #${env.BUILD_NUMBER}. Consultez les logs pour plus de détails.",
                mimeType: 'text/html',
                attachLog: true,
                from: 'molka.kbaier@esprit.tn',
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']]
            )
        }
    }
}
