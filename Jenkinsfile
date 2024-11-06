pipeline {
    agent any

    stages {
        stage("Cloning") {
            steps {
                git branch: "WaelBouaouina_5arctic5-G3",
                    url: "https://github.com/Molka-Kbaier/5arctic5-G3-StationSki"
            }
        }

        stage("Building Image") {
            steps {
                sh "docker build -t wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER} ."
            }
        }

        stage("Tagging Image") {
            steps {
                sh "docker tag wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER} wael975/waelbouaouina-g3-stationski:${env.BUILD_NUMBER}"
            }
        }

        stage("Pushing Image") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    echo "Docker User: $DOCKER_USER"
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
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
