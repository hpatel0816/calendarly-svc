FROM maven:3.8-openjdk-17 AS build

WORKDIR /app
COPY . .

RUN --mount=type=cache,id=m2,target=/usr/share/maven/ref mvn -B clean install -Dmaven.test.skip=true
ENV APPLICATION_NAME "calendarly-svc"

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080