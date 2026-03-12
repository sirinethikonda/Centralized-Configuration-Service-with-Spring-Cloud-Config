# Stage 1: Build the application using Maven wrapper
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Make the wrapper executable
RUN chmod +x ./mvnw
# Download dependencies
RUN ./mvnw dependency:go-offline
COPY src ./src
# Build the JAR file
RUN ./mvnw package -DskipTests

# Stage 2: Create the final lightweight image
FROM eclipse-temurin:21-jre-alpine
RUN apk add --no-cache curl
WORKDIR /app
# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar
# Expose the port
EXPOSE 8888
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
