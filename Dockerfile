FROM openjdk:17-jdk-alpine
WORKDIR /app
EXPOSE 8089
ADD target/gestion-station-ski-1.0.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]