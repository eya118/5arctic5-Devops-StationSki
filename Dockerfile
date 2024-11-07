FROM openjdk:17-jdk-alpine

ENV NEXUS_REPO_URL=http://192.168.2.16:8081/repository/maven-releases
ENV ARTIFACT_GROUP=tn/esprit/spring
ENV ARTIFACT_NAME=gestion-station-ski
ENV ARTIFACT_VERSION=1.0
ENV JAR_NAME=gestion-station-ski-1.0.jar

WORKDIR /app

RUN wget "${NEXUS_REPO_URL}/${ARTIFACT_GROUP}/${ARTIFACT_NAME}/${ARTIFACT_VERSION}/${JAR_NAME}" -O app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]
