FROM maven:3.9.11-eclipse-temurin-21 AS build
 
WORKDIR /app
 
COPY pom.xml .
COPY src ./src
 
RUN mvn clean install
 
FROM eclipse-temurin:21-alpine-3.23
 
WORKDIR /app

 
RUN apk add --no-cache wget

 
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
 
COPY --from=build /app/target/auth-service-0.0.1-SNAPSHOT.jar app.jar
 
EXPOSE 8080
 
ENV SPRING_PROFILES_ACTIVE=docker

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

CMD ["java", "-jar", "app.jar"]

