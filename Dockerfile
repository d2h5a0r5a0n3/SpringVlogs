# Build stage
FROM maven:3.8.8-amazoncorretto-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Runtime stage
FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

COPY src/main/resources/application.properties /app/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
