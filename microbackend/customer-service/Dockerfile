FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Parent POM
COPY pom.xml ./pom.xml

# All module POMs (needed so Maven can resolve the multi-module structure during install -N)
COPY customer-service/pom.xml  customer-service/pom.xml
COPY account-service/pom.xml   account-service/pom.xml
COPY ledger-service/pom.xml    ledger-service/pom.xml
COPY payment-service/pom.xml   payment-service/pom.xml
COPY api-gateway/pom.xml       api-gateway/pom.xml

# Only this module's sources
COPY customer-service/src      customer-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn install -N -DskipTests -q && \
    mvn -pl customer-service package -DskipTests -q

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/customer-service/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
