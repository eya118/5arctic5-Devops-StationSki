FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/gestion-station-ski-2.0.jar app.jar

EXPOSE 8089

ENTRYPOINT ["java","-jar","app.jar"]