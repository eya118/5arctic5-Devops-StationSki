pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://192.168.33.10:9000'  // Replace with the correct SonarQube URL
        NEXUS_URL = 'http://192.168.33.10:8081'      // Replace with the correct Nexus URL
        SONARQUBE_CREDENTIALS = credentials('sonarqube-credentials') // Manage SonarQube credentials via Jenkins
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling the repository from GitHub'
                git(
                    branch: 'MeriemIbrahim-5arctic5-G3',
                    url: 'https://github.com/Molka-Kbaier/5arctic5-G3-StationSki.git'
                )
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
        stage('Packaging') {
            steps {
                echo 'Packaging with Maven...'
                sh 'mvn clean package'
                echo 'Maven Packaging completed!'         
                }
        }

        
        stage("Run Tests and Generate JaCoCo Report") {
            steps {
                echo 'generating JaCoCo report...'
                sh 'mvn clean test jacoco:report'
                echo 'JaCoCo report generated successfully!'
            }
        }

        stage('Verifying') {
            steps {
                echo 'Verifying JaCoCo XML report file...'
                sh 'ls -l target/site/jacoco/jacoco.xml || echo " report JaCoCo not found!"'
            }
        }
                stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withCredentials([usernamePassword(credentialsId: 'sonarqube-credentials', usernameVariable: 'SONAR_USERNAME', passwordVariable: 'SONAR_PASSWORD')]) {
                    sh """
                    mvn sonar:sonar \
                    -Dsonar.host.url=${SONARQUBE_URL} \
                    -Dsonar.login=${SONAR_USERNAME} \
                    -Dsonar.password=${SONAR_PASSWORD} \
                    -Dsonar.projectKey=MeriemIbrahim-5arctic5-G3\
                    -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                    -Dsonar.java.binaries=target/classes \
                    -Dsonar.junit.reportPaths=target/surefire-reports
                    """
                }
                echo 'SonarQube analysis completed!'
            }
        }
        stage('MVN NEXUS') {
            steps {
                echo 'Deploying artifacts to Nexus...'
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh """
                       mvn deploy -DskipTests \
                       -Dnexus.username=$NEXUS_USERNAME \
                       -Dnexus.password=$NEXUS_PASSWORD
                    """
                }
                echo 'Artifacts deployed to Nexus successfully!'
            }
        }
        stage("Docker Build") {
            steps {
                echo "Building Docker image..."
                sh "docker build -t meriemibrahim635/meriemibrahim_g3_stationski ."
                echo 'Docker image built successfully!'
            }
        }

        stage('Pushing to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh 'docker push meriemibrahim635/meriemibrahim_g3_stationski'
                }
            }
        }


        stage('Cleanup Backend') {
            steps {
                echo "Stopping and removing existing backend container (if any)..."
                script {
                    def backend = sh(
                        script: "docker ps -q --filter name=meriem_5arctic5_springbootapp_1",
                        returnStdout: true
                    ).trim()
                    if (backend) {
                        sh "docker stop $backend && docker rm $backend"
                    }
                }
            }
        }
        stage('Cleanup Frontend') {
            steps {
                echo "Stopping and removing existing Frontend container (if any)..."
                script {
                    def Frontend = sh(
                        script: "docker ps -q --filter name=meriem_5arctic5_frontend_1",
                        returnStdout: true
                    ).trim()
                    if (Frontend) {
                        sh "docker stop $Frontend && docker rm $Frontend"
                    }
                }
            }
        }
        stage('Cleanup MySQL Container and Image') {
    steps {
        echo "Removing MySQL container and image (if exists)..."
        script {
            def mysqldb = sh(script: "docker ps -q --filter name=mysqldb", returnStdout: true).trim()
            if (mysqldb) { // Check if MySQL container is running
                sh "docker stop $mysqldb && docker rm $mysqldb"
            }
            def mysqlImage = sh(script: "docker images -q mysql:8.0", returnStdout: true).trim()
            if (mysqlImage) {
                sh "docker rmi -f mysql:8.0"
            }
        }
    }
}


        stage('Running containers') {
            steps {
                echo 'Starting Docker containers...'
                sh 'docker-compose up -d'
                echo 'Containers started!'
            }
        }
               
    }

   
    post {
    success {
        mail to: 'Meriem.ibrahim@esprit.tn',
             subject: "Build Successful: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
             body: "Good news! The build succeeded!"
    }
    failure {
        mail to: 'Meriem.ibrahim@esprit.tn',
             subject: "Build Failed: ${env.JOB_NAME} [${env.BUILD_NUMBER}]",
             body: "Please check the build logs to investigate the issue."
    }
}

}
