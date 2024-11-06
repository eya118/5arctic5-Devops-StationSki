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
                // Build de l'image Docker sans encore tagger
                sh "docker build -t wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER} ."
            }
        }

        stage("Tagging Image") {
            steps {
                // Création du tag supplémentaire pour l'image Docker
                sh "docker tag wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER} wael975/waelbouaouina-g3-stationski:latest"
            }
        }

        stage("Pushing Image") {
            steps {
                script {
                    // Connexion à Docker Hub et push des images taggées
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        // Push de l'image avec son tag spécifique
                        sh "docker push wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER}"
                        // Push de l'image avec le tag 'latest'
                        sh "docker push wael975/waelbouaouina-g3-stationski:latest"
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
