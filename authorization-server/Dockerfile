FROM eclipse-temurin:24-jdk-alpine
RUN apk add --no-cache curl
LABEL maintainer="Anand Bose"
LABEL description="The OAuth2 authorization server for the TODO application"
LABEL version="1.0"
# Set the working directory
WORKDIR /
# Copy the application JAR file into the container
COPY target/*.jar /app.jar
# Expose the port the application runs on
EXPOSE 9000
# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
HEALTHCHECK --interval=30s --timeout=30s --start-period=10s --retries=3 \
    CMD curl -f http://localhost:9000/actuator/health || exit 1

# Optional: Add a label for the image version
LABEL org.opencontainers.image.version="1.0"
# Optional: Add a label for the image source code
LABEL org.opencontainers.image.source="https://github.com/anandbosedev/spring-todo-app"
