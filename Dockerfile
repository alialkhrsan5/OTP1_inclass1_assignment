FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/OTP1_inclass1_assignment-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]