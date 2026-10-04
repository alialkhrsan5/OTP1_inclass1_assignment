FROM eclipse-temurin:21-jdk

WORKDIR /app

# GUI libraries needed by JavaFX
RUN apt-get update && apt-get install -y \
    libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0 mesa-utils wget unzip \
    && rm -rf /var/lib/apt/lists/*

# JavaFX SDK for Linux
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

# Fat JAR built by maven-shade-plugin
COPY target/temp_converter.jar app.jar

# Xming on Windows
ENV DISPLAY=host.docker.internal:0.0

# The database runs on the Windows host, not inside the container
ENV DB_HOST=host.docker.internal

CMD ["java", \
     "--module-path", "/javafx-sdk/lib", \
     "--add-modules", "javafx.controls,javafx.fxml", \
     "-Dprism.order=sw", \
     "-jar", "app.jar"]

# How to run:
# mvn clean package
# docker build -t alii123x/temp-converter-fx .
# docker run --rm -e DISPLAY=host.docker.internal:0.0 alii123x/temp-converter-fx
