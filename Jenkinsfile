pipeline {
    agent any

    environment {
        // Définir les variables d'environnement pour Docker Credentials
        DOCKER_CREDENTIALS_USR = credentials('docker-credentials-username')
        DOCKER_CREDENTIALS_PSW = credentials('docker-credentials-password')
    }

    stages {
        stage("Cloning") {
            steps {
                git branch: "WaelBouaouina_5arctic5-G3",
                    url: "https://github.com/Molka-Kbaier/5arctic5-G3-StationSki"
            }
        }

        stage("Compiling") {
            steps {
                sh "mvn clean compile"
            }
        }

        stage("SonarQube Analysis") {
            steps {
                script {
                    def mvn = tool "M2_HOME"
                    withSonarQubeEnv("SONARQUBE_SERVER") {
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=gestion-station-ski -Dsonar.projectName='gestion-station-ski' -Dsonar.login=${SONARQUBE_TOKEN}"
                    }
                }
            }
        }

        stage("Testing") {
            steps {
                sh "mvn test"
            }
        }

        stage("Packaging") {
            steps {
                sh "mvn package -DskipTests=true"
            }
        }

        stage("Deploy to Nexus") {
            steps {
                sh "mvn clean deploy"
            }
        }

        stage("Building Image") {
            steps {
                // Ajout d'un tag unique basé sur le numéro de build Jenkins
                sh "docker build -t wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER} ."
            }
        }

        stage('Push Image') {
            steps {
                script {
                    // Connexion à Docker Hub avec les credentials
                    withCredentials([usernamePassword(credentialsId: 'docker-credentials-id', usernameVariable: 'DOCKER_CREDENTIALS_USR', passwordVariable: 'DOCKER_CREDENTIALS_PSW')]) {
                        sh "echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin"
                        sh "docker push wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline terminé avec succès !"
        }
        failure {
            echo "Le pipeline a échoué."
        }
    }
}
