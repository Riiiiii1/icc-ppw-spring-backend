FROM gradle:8.10-jdk17 AS builder
WORKDIR /build
COPY build.gradle settings.gradle* ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x gradlew
COPY src ./src
RUN ./gradlew build -x test --no-daemon


FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache curl \
    && addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=builder /build/build/libs/fundamentos01-api.jar app.jar
RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl --fail --silent http://localhost:8080/api/actuator/health || exit 1

ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]