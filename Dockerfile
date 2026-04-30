FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/retirement-savings-1.0-SNAPSHOT.jar app.jar
EXPOSE 5477
ENTRYPOINT ["java", "-jar", "app.jar"]
