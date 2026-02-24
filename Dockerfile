# Use a multi-stage build to produce a small runtime image
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml mvnw .mvn/ ./
COPY src ./src
# Package the application; skip tests to speed up iterative builds
RUN mvn clean -B -DskipTests package -U

# Runtime image
FROM eclipse-temurin:17-jre-jammy
# instalar cliente postgres para pg_isready
RUN apt-get update && apt-get install -y --no-install-recommends postgresql-client && rm -rf /var/lib/apt/lists/*
COPY --from=build /workspace/target/goc-0.0.1-SNAPSHOT.jar /app/app.jar
# copiar scripts de inicialização
COPY init.sql /app/init.sql
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]
