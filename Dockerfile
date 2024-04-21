FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu AS build
FROM node

ARG JAVA_HOME
ENV JAVA_HOME $JAVA_HOME
ENV PATH="$JAVA_HOME/bin:${PATH}"

WORKDIR /home/gradle/project
COPY . .
RUN mkdir -p /root/.gradle && \
    echo -e "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties
RUN chmod +x ./gradlew && ./gradlew clean build

FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/codeython-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application-prod.yaml resources/application-prod.yaml
CMD ["java", "-jar", "app.jar", "--spring.config.location=resources/application-prod.yaml"]
