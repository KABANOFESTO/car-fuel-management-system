
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .

COPY backend/pom.xml backend/
COPY cli-client/pom.xml cli-client/

COPY backend/src backend/src
COPY cli-client/src cli-client/src

RUN mvn clean package -pl backend -am -DskipTests

FROM tomcat:9.0-jdk17-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/backend/target/car-management-backend.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
