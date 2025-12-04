# Stage 1: Build the application
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy the Maven build file and source code
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

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
COPY --from=build /app/target/${ARTIFACT_NAME}.jar app.jar

# Define the port the gRPC server is listening on (9092 from application.yml)
EXPOSE 9092

# Command to run the JAR when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]