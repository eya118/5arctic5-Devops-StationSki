pipeline {
    agent any

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
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=gestion-station-ski -Dsonar.projectName='gestion-station-ski' -Dsonar.login=sqp_cc9cc391fa75d8fc9444640031a87972caf8a4c0"
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

        stage("Pushing Image") {
            steps {
                // Utilisation de docker.withRegistry pour se connecter au registre Docker
                docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                    echo "Docker login réussi, envoi de l'image..."
                    sh "docker push wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER}"
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
