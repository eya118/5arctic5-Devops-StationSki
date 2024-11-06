pipeline {
    agent any

    environment {
        SONARQUBE_URL = 'http://192.168.101.6:9000'  // URL de SonarQube
        NEXUS_URL = 'http://192.168.101.6:8081'      // URL de Nexus
    }

    stages {
        stage('Checkout GIT') {
            steps {
                echo 'Pulling the repository from GitHub...'
                git(
                    branch: 'aminekbaier_5ARCTIC5_G3',
                    url: 'https://github.com/Molka-Kbaier/5arctic5-G3-StationSki.git'
                )
                echo 'Latest commit information:'
                sh 'git log -1'
            }
        }

        stage('Build with Maven') {
            steps {
                echo 'Starting Maven compilation...'
                sh 'mvn clean compile'
                echo 'Maven compilation completed!'
            }
        }

        stage('Run Unit Tests') {
            steps {
                echo 'Running JUnit and Mockito tests...'
                sh 'mvn test'
                echo 'Unit tests completed successfully!'
            }
        }

        stage('Generate JaCoCo Coverage Report') {
            steps {
                echo 'Generating JaCoCo coverage report...'
                sh 'mvn clean test jacoco:report'
                echo 'JaCoCo coverage report generated!'
            }
        }

        stage('Verify JaCoCo Report') {
            steps {
                echo 'Checking if JaCoCo XML report file exists...'
                script {
                    def fileExists = sh(script: 'ls target/site/jacoco/jacoco.xml', returnStatus: true)
                    if (fileExists != 0) {
                        echo "JaCoCo report not found!"
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'sonarqube-credentials', usernameVariable: 'SONARQUBE_CREDENTIALS_USR', passwordVariable: 'SONARQUBE_CREDENTIALS_PSW')]) {
                    echo 'Starting SonarQube analysis...'
                    sh """
                       mvn sonar:sonar \
                       -Dsonar.host.url=${SONARQUBE_URL} \
                       -Dsonar.login=${SONARQUBE_CREDENTIALS_USR} \
                       -Dsonar.password=${SONARQUBE_CREDENTIALS_PSW} \
                       -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                    echo 'SonarQube analysis completed!'
                }
            }
        }

        stage('Deploy Artifacts to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-credentials', usernameVariable: 'NEXUS_CREDENTIALS_USR', passwordVariable: 'NEXUS_CREDENTIALS_PSW')]) {
                    echo 'Deploying artifacts to Nexus...'
                    sh """
                       mvn deploy -DskipTests \
                       -Dnexus.username=${NEXUS_CREDENTIALS_USR} \
                       -Dnexus.password=${NEXUS_CREDENTIALS_PSW}
                    """
                    echo 'Artifacts deployed to Nexus successfully!'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo "Building Docker image..."
                sh "docker build -t aminekbaier/gestionstation-ski:1.0.0 ."
                echo "Docker image built successfully!"
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    echo "Pushing Docker image to Docker Hub..."
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    sh 'docker push aminekbaier/gestionstation-ski:1.0.0'
                }
                echo "Docker image pushed successfully!"
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo "Deploying application with Docker Compose..."
                sh "docker-compose down || true"
                sh "docker-compose up -d"
                echo "Deployment with Docker Compose completed!"
            }
        }

        stage('Send Email Notification') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'gmailcredential', usernameVariable: 'GMAIL_CREDENTIALS_USR', passwordVariable: 'GMAIL_CREDENTIALS_PSW')]) {
                        emailext(
                            to: 'amine.kbaier@esprit.tn',
                            subject: "Pipeline Report - Station University",
                            body: '''\
                                Bonjour Amine,

                                Voici le rapport du pipeline:

                                - **Git Pull** : Réussi
                                - **Build Maven** : Réussi
                                - **Tests Unitaires** : Réussi
                                - **Analyse SonarQube** : Réussi
                                - **Déploiement Nexus** : Réussi
                                - **Build Backend** : Réussi
                                - **Déploiement Docker** : Réussi

                                **Résumé** : Le pipeline s'est terminé avec succès.

                                Cordialement,
                                Équipe DevOps
                                ''',
                            from: "${GMAIL_CREDENTIALS_USR}",
                            replyTo: "${GMAIL_CREDENTIALS_USR}",
                            mimeType: 'text/plain'
                        )
                    }
                }
            }
        }
    }

    post {
        failure {
            script {
                withCredentials([usernamePassword(credentialsId: 'gmailcredential', usernameVariable: 'GMAIL_CREDENTIALS_USR', passwordVariable: 'GMAIL_CREDENTIALS_PSW')]) {
                    emailext(
                        to: 'amine.kbaier@esprit.tn',
                        subject: "Échec du Pipeline - Station University",
                        body: """\
                            Bonjour Amine,

                            Malheureusement, le pipeline **Station University** a rencontré un problème.

                            - **Étape en Échec** : ${env.STAGE_NAME}
                            - **Erreur** : Le pipeline a échoué à l'étape ${env.STAGE_NAME}. Veuillez consulter les logs Jenkins pour plus de détails sur la nature de l'erreur.

                            **Résumé** : Le pipeline n'a pas pu se terminer avec succès. Nous vous recommandons de vérifier immédiatement les détails dans la console Jenkins pour corriger l'erreur.

                            **Conseils** :
                            - Vérifiez les fichiers de configuration liés à cette étape.
                            - Assurez-vous que tous les services nécessaires sont opérationnels.
                            - Consultez l'historique des builds pour des erreurs similaires.

                            Cordialement,
                            Équipe DevOps
                            """,
                        from: "${GMAIL_CREDENTIALS_USR}",
                        replyTo: "${GMAIL_CREDENTIALS_USR}",
                        mimeType: 'text/plain'
                    )
                }
            }
        }
    }
}
