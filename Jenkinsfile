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
                // Compilation du projet Maven
                sh "mvn clean compile"
            }
        }

        stage("SonarQube Analysis") {
            steps {
                script {
                    // Définir l'installation Maven
                    def mvn = tool "M2_HOME"
                    // Configuration de l'environnement SonarQube
                    withSonarQubeEnv("SONARQUBE_SERVER") {
                        // Exécution de la commande Maven avec le jeton d'authentification
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=gestion-station-ski -Dsonar.projectName='gestion-station-ski' -Dsonar.login=sqp_cc9cc391fa75d8fc9444640031a87972caf8a4c0"
                    }
                }
            }
        }

        stage("Testing") {
            steps {
                // Exécution des tests
                sh "mvn test"
            }
        }

        stage("Packaging") {
            steps {
                // Packaging du projet
                sh "mvn package -DskipTests=true"
            }
        }

        stage("Deploy to Nexus") {
            steps {
                // Déploiement vers Nexus en sautant les tests
                sh "mvn clean deploy"
            }
        }

        stage("Building Image") {
            steps {
                // Construire l'image Docker
                sh "docker build -t wael975/waelbouaouina-g3-stationski ."
            }
        }

        stage("Pushing Image") {
            steps {
                // Se connecter à Docker Hub avec les identifiants
                withCredentials([usernamePassword(credentialsId: 'dockerhub-wael975',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker logout"
                    sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                    // Pousser l'image vers Docker Hub
                    sh "docker push wael975/waelbouaouina-g3-stationski"
                }
            }
        }
    }

    post {
        success {
            // Message de réussite du pipeline
            echo "Pipeline terminé avec succès !"
        }
        failure {
            // Message d'échec du pipeline
            echo "Le pipeline a échoué."
        }
    }
}
