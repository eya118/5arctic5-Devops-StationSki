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

        stage("SonarQube") {
            steps {
                // Analyse SonarQube, suppose que le plugin SonarQube est bien configuré
                withSonarQubeEnv('SonarQube') {
                    sh "mvn clean verify sonar:sonar -Dsonar.verbose=true"
                }
            }
        }

        stage("Testing") {
            steps {
                sh "mvn test"
            }
        }

        stage("JUnit Report") {
            steps {
                // Archive les résultats des tests sous forme de rapport JUnit
                junit '**/target/surefire-reports/*.xml'
            }
        }


        stage("Deploy to Nexus") {
            steps {
                sh "mvn clean deploy -DskipTests"
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

