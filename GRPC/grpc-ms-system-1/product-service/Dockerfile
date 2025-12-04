# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# 1. Copy the ROOT POM file (The Parent POM)
COPY ../pom.xml .

# Copy the Maven build file and source code
COPY . .

# Build the application and generate the JAR
# The build step ensures all dependencies are downloaded and the application is compiled.
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final, smaller runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Définit l'argument ARTIFACT_NAME (sera passé par docker-compose)
ARG ARTIFACT_NAME

# Copy the built JAR file from the 'build' stage
# The JAR file name is typically 'service-name-version.jar'. Adjust as needed.
COPY --from=build /target/${ARTIFACT_NAME}.jar app.jar

# Define the port the gRPC server is listening on (9090 from application.yml)
EXPOSE 9090

# Command to run the JAR when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]