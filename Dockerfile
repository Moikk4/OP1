# Build stage: compile and test the Maven project
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage: JRE + X11 libraries so the JavaFX GUI can render on an X server
FROM eclipse-temurin:17-jre
RUN apt-get update && apt-get install -y --no-install-recommends \
        libxrender1 libxtst6 libxi6 libgl1 libgtk-3-0 \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY --from=build /app/target/op1-1.0-SNAPSHOT.jar app.jar
# Default: launch the JavaFX GUI (needs DISPLAY, e.g. Xming).
# Override with: docker run <image> --cli   for a headless console demo.
ENTRYPOINT ["java", "-jar", "app.jar"]
CMD []
