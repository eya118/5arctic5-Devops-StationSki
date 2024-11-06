# Étape 1 : Utiliser une image Maven pour construire l'application
FROM openjdk:17-jdk-alpine

# Définir les variables d'environnement pour Nexus
ENV NEXUS_REPO_URL=http://192.168.2.16:8081/repository/maven-releases
ENV ARTIFACT_GROUP=tn/esprit/spring
ENV ARTIFACT_NAME=gestion-station-ski
ENV ARTIFACT_VERSION=1.0
ENV JAR_NAME=gestion-station-ski-1.0.jar

# Définir le répertoire de travail
WORKDIR /app

# Télécharger le fichier JAR depuis Nexus
RUN wget "${NEXUS_REPO_URL}/${ARTIFACT_GROUP}/${ARTIFACT_NAME}/${ARTIFACT_VERSION}/${JAR_NAME}" -O app.jar

# Exposer le port de l'application
EXPOSE 8089

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
