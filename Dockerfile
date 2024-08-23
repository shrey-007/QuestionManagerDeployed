# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Add the Spring Boot application's jar file
COPY QuestionsManager-0.0.1-SNAPSHOT.jar /app/application.jar

# Expose the port that your Spring Boot application listens on
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "application.jar"]
