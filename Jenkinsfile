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

        stage('MVN MOCKITO') {
            steps {
                echo 'Running Mockito tests and generating coverage report...'
                sh 'mvn test jacoco:report'
                echo 'Mockito tests completed!'
            }
        }

        stage('MVN SONARQUBE') {
            steps {
                echo 'Running SonarQube analysis...'
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


        // Ajoutez les autres étapes ici...
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
                attachLog: true
            )
        }
        failure {
            echo 'Pipeline failed. Please check the logs for more details.'
            emailext(
                to: 'molka.kbaier@esprit.tn',
                subject: "Build Failure: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Le pipeline a échoué pour le job ${env.JOB_NAME} - build #${env.BUILD_NUMBER}. Consultez les logs pour plus de détails.",
                mimeType: 'text/html',
                attachLog: true
            )
        }
    }
}
