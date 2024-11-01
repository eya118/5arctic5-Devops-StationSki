# Use an official OpenJDK image to run Java applications
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build context into the container
RUN wget -O gestion-station-ski-1.0.jar  http://192.168.33.10:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Expose the port that your Spring Boot app will run on (8089)
EXPOSE 8089

# Run the jar file when the container starts
ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar"]
