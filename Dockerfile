# Étape 1 : Utiliser une image Maven pour construire l'application
FROM openjdk:17-jdk-alpine

# Définir le répertoire de travail
WORKDIR /app

# Variables pour Nexus Repository
ARG NEXUS_REPO_URL="http://your-nexus-repository/repository/maven-releases"
ARG JAR_NAME="5ARCTIC5-G3-StationSki.jar"
ARG ARTIFACT_GROUP="tn/esprit/spring"
ARG ARTIFACT_VERSION="1.0"

# Télécharger le fichier JAR depuis Nexus
RUN wget "${NEXUS_REPO_URL}/${ARTIFACT_GROUP}/${ARTIFACT_VERSION}/${JAR_NAME}" -O app.jar

# Exposer le port de l'application
EXPOSE 8089

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
