pipeline {
    agent any

    stages {
        stage('code clone') {
            steps {
                git branch: 'WaelBouaouina_5arctic5-G3',
                    url: 'https://github.com/Molka-Kbaier/5arctic5-G3-StationSki',
                    
            }
        }

        stage('Maven Compilation') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn clean test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml' // Rapporte les résultats des tests
                }
            }
        }

        stage('Packaging') {
            steps {
                sh 'mvn clean package'
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Le pipeline a échoué.'
        }
    }
}
