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
                sh "docker build -t wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER} ."
            }
        }

        stage('Docker Push') {
            steps {
                echo "======== Pushing Docker Image to Docker Hub ========"
                withCredentials([string(credentialsId: 'dockerhub-token', variable: 'DOCKER_TOKEN')]) {
                    sh 'echo $DOCKER_TOKEN | docker login -u wael975 --password-stdin' 
                    sh "docker push wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER}"
                }
            }
        }

        stage("Running Docker Compose") {
            steps {
                script {
                    // Vérifiez si Docker Compose est installé, sinon, installez-le ici.
                    sh "docker-compose -v" // Affiche la version de Docker Compose

                    // Lancer les services de Docker Compose
                    sh "docker-compose -f docker-compose.yml up -d"
                }
            }
        }

        stage("Testing with Docker Compose") {
            steps {
                script {
                    // Attendez que l'application Spring soit prête (un peu de délai si nécessaire)
                    sleep 10  // Attendez 10 secondes pour laisser les services démarrer

                    // Vous pouvez exécuter des tests sur votre application Spring ici
                    // Par exemple, utiliser curl pour vérifier que l'application fonctionne
                    sh 'curl -f http://localhost:8089/api/health'  // Changez l'URL selon votre endpoint de santé
                }
            }
        }

        stage("Stopping Docker Compose") {
            steps {
                // Arrêtez les services de Docker Compose
                sh "docker-compose -f docker-compose.yml down"
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
