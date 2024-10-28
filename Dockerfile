# Use an official OpenJDK image to run Java applications
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build context into the container
#RUN wget -O tp-foyer-1.0.jar http://your-nexus-repo-url/path/to/tp-foyer-1.0.jar

# Expose the port that your Spring Boot app will run on (8089)
EXPOSE 8089

# Run the jar file when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]