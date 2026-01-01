
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .

COPY backend/pom.xml backend/
COPY cli-client/pom.xml cli-client/

COPY backend/src backend/src
COPY cli-client/src cli-client/src

RUN mvn clean package -pl cli-client -am -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/cli-client/target/cli-client-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
