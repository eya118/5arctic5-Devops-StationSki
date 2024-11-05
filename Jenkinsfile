pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo "======== Checking out source code ========"
                // Récupérer le code source depuis GitHub
                git branch: 'molkakbaier_5ARCTIC5_G3',
                    url: 'https://github.com/Molka-Kbaier/5arctic5-G3-StationSki.git'
            }
        }

        stage('Compiling') {
            steps {
                echo "======== Compiling with Maven ========"
                // Compilation du code avec Maven
                sh "mvn clean compile"
            }
        }

        stage('Testing') {
            steps {
                echo "======== Running Tests with Maven ========"
                // Exécution des tests
                sh "mvn test"
            }
        }

        stage('Packaging') {
            steps {
                echo "======== Packaging the Application ========"
                // Générer le fichier JAR avec Maven
                sh "mvn clean package"
            }
        }

        stage('Docker Build') {
            steps {
                echo "======== Building Docker Image ========"
                // Construire l'image Docker
                sh "docker build -t molkak/station-skii:1.0.0 ."
            }
        }

        stage('Docker Push') {
            steps {
                echo "======== Pushing Docker Image to Docker Hub ========"
                // Pousser l'image Docker vers Docker Hub
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USERNAME',
                    passwordVariable: 'DOCKER_PASSWORD'
                )]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    sh 'docker push molkak/station-skii:1.0.0'
                }
            }
        }

        stage('Cleanup Backend Container') {
            steps {
                echo "======== Stopping Existing Backend Container (if any) ========"
                script {
                    def backendContainer = sh(
                        script: "docker ps -q --filter name=dockerpipline_backend_1",
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
                // Arrêter et supprimer le conteneur et l'image MySQL si présents
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
                // Relancer les conteneurs avec Docker Compose
                sh "docker-compose down || true"  // Éviter une erreur si aucun conteneur n'est actif
                sh "docker-compose up -d"
            }
        }
    }

    post {
        always {
            echo "======== Cleaning up Docker Resources ========"
            // Nettoyage des ressources inutilisées
            sh "docker system prune -f || true"
        }
        success {
            echo "======== Build and Deployment Successful! ========"
        }
        failure {
            echo "======== Build or Deployment Failed. Check the logs. ========"
        }
    }
}
