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
                    withSonarQubeEnv("SonarQube Token") {
                        sh "${mvn}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=gestion-station-ski -Dsonar.projectName='gestion-station-ski'"
                    }
                }
            }
        }

        stage("Testing") {
            steps {
                sh "mvn test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'  // Archive des résultats de tests
                }
            }
        }

        stage("Packaging") {
            steps {
                sh "mvn package"
            }
        }

        stage("Deploy to Nexus") {
            steps {
                sh "mvn clean deploy -DskipTests"
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
