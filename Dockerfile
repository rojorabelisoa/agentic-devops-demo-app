# Runtime image only: the jar is built and tested by the CI stage before this build.
FROM eclipse-temurin:21.0.12.1_1-jre-noble@sha256:000fd431958bc81a24abe1e8e5f0f0fd3ae365a594bd50aadb20696805f9408c
WORKDIR /app
COPY target/demo-app.jar app.jar
USER 10001:10001
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/app/app.jar"]
