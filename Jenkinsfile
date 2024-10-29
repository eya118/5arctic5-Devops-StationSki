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
                sh "mvn clean install"
            }
        }

        stage("Testing") {
            steps {
                sh "mvn clean test"
            }
            
        }

        stage("Packaging") {
            steps {
                sh "mvn clean package"
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
