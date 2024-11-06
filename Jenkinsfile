pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://192.168.101.6:9000'                  // URL de SonarQube
        NEXUS_URL = 'http://192.168.101.6:8081'                       // URL de Nexus
        SONARQUBE_CREDENTIALS = credentials('sonarqube-credentials')  // Credentials SonarQube
        NEXUS_CREDENTIALS = credentials('nexus-credentials')          // Credentials Nexus
        GMAIL_CREDENTIALS = credentials('gmailcredential')            // Credentials Gmail pour notifications e-mail
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling the repository from GitHub...'
                git(
                    branch: 'aminekbaier_5ARCTIC5_G3',
                    url: 'https://github.com/eya118/5arctic5-G3-timesheet-devops.git'
                )
                echo 'Latest commit information:'
                sh 'git log -1'
            }
        }

        stage('Build with Maven') {
            steps {
                echo 'Starting Maven compilation...'
                sh 'mvn clean compile'
                echo 'Maven compilation completed!'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo 'Running JUnit and Mockito tests...'
                sh 'mvn test'
                echo 'Unit tests completed successfully!'
            }
        }

        stage('Generate JaCoCo Coverage Report') {
            steps {
                echo 'Generating JaCoCo coverage report...'
                sh 'mvn clean test jacoco:report'
                echo 'JaCoCo coverage report generated!'
            }
        }

        stage('Verify JaCoCo Report') {
            steps {
                echo 'Checking if JaCoCo XML report file exists...'
                sh 'ls -l target/site/jacoco/jacoco.xml || echo "JaCoCo report not found!"'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Starting SonarQube analysis...'
                sh """
                   mvn sonar:sonar \
                   -Dsonar.host.url=${SONARQUBE_URL} \
                   -Dsonar.login=${SONARQUBE_CREDENTIALS_USR} \
                   -Dsonar.password=${SONARQUBE_CREDENTIALS_PSW} \
                   -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                """
                echo 'SonarQube analysis completed!'
            }
        }

        stage('Deploy Artifacts to Nexus') {
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

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh "docker build -t aminekbaier/station-ski:1.0.0 ."
                echo "Docker image built successfully!"
            }
        }

        stage('Push Docker Image') {
            steps {
                echo "Pushing Docker image to Docker Hub..."
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    sh 'docker push aminekbaier/station-ski:1.0.0'
                }
                echo "Docker image pushed successfully!"
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "Deploying application with Docker Compose..."
                sh "docker-compose down || true"
                sh "docker-compose up -d"
                echo "Deployment with Docker Compose completed!"
            }
        }

        stage('Send Email Notification') {
            steps {
                emailext(
                    to: 'amine.kbaier@esprit.tn',
                    subject: "Pipeline Report - Kaddem University",
                    body: '''\
                        Hello,

                        Here is the pipeline report:

                        - **Git Pull**: Successful
                        - **Maven Build**: Successful
                        - **Unit Tests**: Successful
                        - **SonarQube Analysis**: Successful
                        - **Nexus Deployment**: Successful
                        - **Backend Build**: Successful
                        - **Docker Deployment**: Successful

                        **Summary**: The pipeline completed successfully.

                        Regards,
                        DevOps Team
                        ''',
                    from: "${GMAIL_CREDENTIALS_USR}",
                    replyTo: "${GMAIL_CREDENTIALS_USR}",
                    mimeType: 'text/plain'
                )
            }
        }
    }

    post {
        always {
            echo "Cleaning up Docker resources..."
            sh "docker system prune -f || true"
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            mail to: 'team@example.com',
                 subject: "Erreur dans le pipeline : ${currentBuild.fullDisplayName}",
                 body: """\
                    Le pipeline a échoué à l'étape: ${currentBuild.currentResult}.

                    Pour plus de détails, consultez les logs dans Jenkins.

                    Cordialement,
                    L'équipe DevOps
                    """
        }
    }
}
