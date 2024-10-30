# Étape 1 : Utiliser une image Maven pour construire l'application
FROM openjdk:17-jdk-alpine


# Définir le répertoire de travail
WORKDIR /app

# Copier l'archive JAR de l'étape de build
COPY target/5ARCTIC5-G3-StationSki.jar app.jar

# Exposer le port de l'application (adapter selon votre application si nécessaire)
EXPOSE 8089

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
