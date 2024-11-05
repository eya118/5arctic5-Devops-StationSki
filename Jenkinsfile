pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://192.168.101.4:9000'  // Remplace avec l'IP ou URL correcte de SonarQube
        NEXUS_URL = 'http://192.168.101.4:8081'      // Remplace avec l'IP ou URL correcte de Nexus
        SONARQUBE_CREDENTIALS = credentials('sonarqube-credentials') // Gestion des identifiants via Jenkins
        NEXUS_CREDENTIALS = credentials('nexus-credentials')
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling the repository from GitHub'
                git(
                    branch: 'master',
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
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs for more details.'
        }
    }
}
