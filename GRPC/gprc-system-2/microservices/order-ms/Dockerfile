# Utilise une image de base Java 17
FROM maven:3.9.6-eclipse-temurin-21-alpine

# Définit l'argument ARTIFACT_NAME (sera passé par docker-compose)
ARG ARTIFACT_NAME

# Copie le JAR construit dans le container sous le nom app.jar
COPY target/${ARTIFACT_NAME}.jar app.jar

# Exécute l'application Spring Boot
ENTRYPOINT ["java","-jar","/app.jar"]