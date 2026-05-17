FROM eclipse-temurin:21-jdk-alpine

# Create a dedicated directory for the app
WORKDIR /app

# Copy the compiled Java application
COPY backend/target/*.jar app.jar

# Copy the frontend folder into the container's working directory
COPY frontend/ frontend/

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]