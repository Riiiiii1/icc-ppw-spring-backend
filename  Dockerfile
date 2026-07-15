# ETAPA 1: BUILD
FROM gradle:8.10-jdk17 AS builder
WORKDIR /build
COPY build.gradle settings.gradle* ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x gradlew
COPY src ./src
RUN ./gradlew build -x test --no-daemon

# ETAPA 2: RUNTIME
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app
COPY --from=builder /build/build/libs/fundamentos01-api.jar app.jar
RUN chown spring:spring app.jar
USER spring:spring
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Xms256m", "-Xmx512m", "-jar", "app.jar"]