FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml ./pom.xml

COPY customer-service/pom.xml  customer-service/pom.xml
COPY account-service/pom.xml   account-service/pom.xml
COPY ledger-service/pom.xml    ledger-service/pom.xml
COPY payment-service/pom.xml   payment-service/pom.xml
COPY api-gateway/pom.xml       api-gateway/pom.xml

COPY ledger-service/src        ledger-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn install -N -DskipTests -q && \
    mvn -pl ledger-service package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/ledger-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
