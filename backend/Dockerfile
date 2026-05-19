FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

COPY backend/ backend/
WORKDIR /build/backend

RUN mvn clean checkstyle:check verify -Dcheckstyle.failsOnError=false -Dcheckstyle.failOnViolation=false

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /build/backend/target/*.jar app.jar

COPY frontend/ frontend/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]