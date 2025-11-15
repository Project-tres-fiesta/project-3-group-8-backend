# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace

# Copy Gradle wrapper & config first to leverage Docker cache
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle
RUN chmod +x gradlew

# Copy source
COPY src src

# Build fat jar (Spring Boot)
RUN ./gradlew bootJar --no-daemon

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Optional JVM options (can be overridden in Heroku config vars)
ENV JAVA_OPTS=""

# Copy the jar produced above (you set it to app.jar in build.gradle)
COPY --from=build /workspace/build/libs/app.jar app.jar

# Heroku sets $PORT; expose and run the jar on that port
CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
