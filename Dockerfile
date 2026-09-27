# Build stage: compile and test the Maven project
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage: small JRE image with only the built jar
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/op1-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-cp", "app.jar", "TemperatureConverter"]

