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
                    sh "docker-compose -v" 
                    sh "docker-compose -f docker-compose.yml up -d"
                }
            }
        }

        stage("Stopping Docker Compose") {
            steps {
               sh "docker-compose -f docker-compose.yml down"
            }
        }
    }

    post {
        always {
            // Sauvegarder les logs
            archiveArtifacts artifacts: '**/target/*.log', allowEmptyArchive: true
        }
        success {
            echo "Pipeline terminé avec succès !"
            // Notification de succès
            emailext subject: "Pipeline Succès : ${currentBuild.fullDisplayName}",
                     body: "Le pipeline s'est terminé avec succès. Voir les détails ici : ${env.BUILD_URL}",
                     to: 'bouaouinawael1@gmail.com'

        }
        failure {
            echo "Le pipeline a échoué."
            // Notification d'échec
            emailext subject: "Pipeline Échec : ${currentBuild.fullDisplayName}",
                     body: "Le pipeline a échoué. Voir les détails ici : ${env.BUILD_URL}",
                     to: 'bouaouinawael1@gmail.com'

        }
    }
}
