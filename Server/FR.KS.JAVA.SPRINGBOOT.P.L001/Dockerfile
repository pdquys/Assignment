# =========================
# 1) Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# cache deps
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# build
COPY src ./src
RUN mvn -q -DskipTests package

# =========================
# 2) Runtime stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

# run as non-root
RUN useradd -m appuser
USER appuser

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
