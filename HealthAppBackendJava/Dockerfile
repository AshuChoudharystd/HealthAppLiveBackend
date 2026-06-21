FROM maven:3.9.11-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml ./
RUN mvn --batch-mode --no-transfer-progress dependency:go-offline

COPY src ./src
RUN mvn --batch-mode --no-transfer-progress clean package -DskipTests

FROM eclipse-temurin:17-jre-noble AS runtime

RUN groupadd --system app && useradd --system --gid app --home-dir /app app

WORKDIR /app

COPY --from=build --chown=app:app /workspace/target/*.jar app.jar

RUN mkdir -p /app/uploads/profile-pictures \
    && chown -R app:app /app

USER app

ENV FILE_STORAGE_LOCAL_DIRECTORY=/app/uploads/profile-pictures

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
