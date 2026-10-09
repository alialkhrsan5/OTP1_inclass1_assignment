# Temperature Converter

JavaFX application that converts temperatures between Celsius, Fahrenheit and
Kelvin and saves every conversion into a MariaDB database.

Author: Ali Al-Khrsan
Course: OTP1 / AD Sprint 3, Fall 2026

- GitHub: https://github.com/alialkhrsan5/OTP1_inclass1_assignment
- Docker Hub: https://hub.docker.com/r/alii123x/temp-converter-fx

---

## 1. Assignment Description

The in-class assignments of this course build on each other, so this repository
contains one application instead of separate exercises.

What was required:

- Temperature conversion logic with JUnit unit tests
- JaCoCo code coverage report
- A Jenkins pipeline that builds, tests and reports coverage
- A Dockerfile, and the image pushed to Docker Hub by the pipeline
- A JavaFX user interface
- A database with at least two related tables, used by the interface
- The application must run both locally and as a Docker image

---

## 2. Technologies & Tools Used

- Java 21
- JavaFX 21.0.6 (controls, fxml)
- Maven 3.9.16
- MariaDB 11.8 with mariadb-java-client 3.4.1
- JUnit 5 (Jupiter) 5.11.3
- JaCoCo 0.8.11
- maven-shade-plugin 3.6.0
- Jenkins (pipeline, Jenkinsfile)
- Docker and Docker Hub
- Xming (X server, used to show the GUI from the container)
- IntelliJ IDEA, HeidiSQL, Git/GitHub

---

## 3. Design Approach & Implementation Method

The application is divided into layers:

```
    JavaFX UI      Main
         |
    DAO layer      TemperatureUnitDAO, TempRecordDAO
         |
    Database       DBConnection -> MariaDB
```

The conversion logic is in a separate class `TempCalculator`, which has no
database or UI code. That is what makes it possible to unit test it.

### Classes

| Class | Purpose |
|---|---|
| `Main` | JavaFX interface: input field, conversion selector, result, history table |
| `TempCalculator` | Conversion formulas and validation |
| `TemperatureUnit` | One row of the `temperature_unit` table |
| `TempRecord` | One row of the `temp_record` table |
| `TemperatureUnitDAO` | Reads the conversion types from the database |
| `TempRecordDAO` | Saves a conversion and reads the history |
| `DBConnection` | Creates the database connection |

### Database

Two related tables:

```
temperature_unit                 temp_record
id          PK  <--------------  temperature_unit_id  FK
unit_name                        id                   PK
                                 input_value
                                 result_value
                                 created_at
```

```sql
CREATE DATABASE IF NOT EXISTS svg_temp_db;
USE svg_temp_db;

CREATE TABLE temperature_unit (
    id INT AUTO_INCREMENT PRIMARY KEY,
    unit_name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE temp_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    input_value DOUBLE NOT NULL,
    result_value DOUBLE NOT NULL,
    temperature_unit_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_temp_unit FOREIGN KEY (temperature_unit_id)
        REFERENCES temperature_unit(id)
);

INSERT INTO temperature_unit (unit_name) VALUES
('Fahrenheit to Celsius'),
('Celsius to Fahrenheit'),
('Kelvin to Celsius');

CREATE USER IF NOT EXISTS 'tempuser'@'localhost' IDENTIFIED BY 'temp123';
CREATE USER IF NOT EXISTS 'tempuser'@'%' IDENTIFIED BY 'temp123';
GRANT ALL PRIVILEGES ON svg_temp_db.* TO 'tempuser'@'localhost';
GRANT ALL PRIVILEGES ON svg_temp_db.* TO 'tempuser'@'%';
FLUSH PRIVILEGES;
```

The conversion types are read from the database when the application starts, so
they are not hard-coded in Java.

### Decisions made during coding

- `DBConnection` reads the host, port, database, user and password from
  environment variables. The same JAR works locally (`localhost`) and in the
  container (`host.docker.internal`) without rebuilding.
- A separate database user `tempuser` is used instead of root. The `'%'` grant is
  needed because the container connects from outside localhost.
- maven-shade-plugin packs the dependencies into one JAR, because the container
  does not have Maven.
- The Dockerfile downloads the Linux JavaFX SDK, because JavaFX native libraries
  are platform specific.

---

## 4. Testing & Quality Assurance Steps

### Automated tests

15 JUnit 5 tests in three test classes, run with `mvn test` and on every Jenkins
build.

| Test class | Tests |
|---|---|
| `TempCalculatorTest` | 11 |
| `TemperatureUnitTest` | 2 |
| `TempRecordTest` | 2 |

### Test cases & results

| Test case | Input | Expected | Result |
|---|---|---|---|
| Fahrenheit to Celsius | 32 °F | 0 °C | pass |
| Fahrenheit to Celsius | 212 °F | 100 °C | pass |
| Celsius to Fahrenheit | 37 °C | 98.6 °F | pass |
| Kelvin to Celsius | 300 K | 26.85 °C | pass |
| Kelvin to Celsius | 0 K | −273.15 °C | pass |
| Extreme temperature, cold | −41 °C | true | pass |
| Extreme temperature, hot | 51 °C | true | pass |
| Extreme temperature, boundaries | −40 °C, 50 °C | false | pass |
| Validation, below absolute zero | −300 °C | exception | pass |
| Validation, negative Kelvin | −1 K | exception | pass |
| Unknown conversion type | "Celsius to Kelvin" | exception | pass |

Result: `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0`

Doubles are compared with a delta of `0.0001`, because floating point values are
not exact. The boundary values −40 °C and 50 °C are tested as well, because
`isExtremeTemperature` uses strict comparisons and those values are not extreme.

### Code coverage

JaCoCo runs in the Maven test phase and the report is published by the Jenkins
pipeline. The HTML report is at `target/site/jacoco/index.html`.

### Manual testing

| Step | Result |
|---|---|
| Start with `mvn javafx:run` | Window opens, conversion types loaded from the database |
| 90 "Fahrenheit to Celsius" | 32.22, row added to the table |
| 89 "Celsius to Fahrenheit" | 192.2, row added |
| 99 "Kelvin to Celsius" | −174.15, row added |
| Text instead of a number | Error dialog shown |
| Check `temp_record` in HeidiSQL | Rows saved correctly |
| Run the Docker image with Xming | Same window opens from the container |

---

## 5. How to Run

### Prerequisites

- JDK 21
- Maven
- MariaDB on `localhost:3306`
- Docker Desktop and Xming (only for the Docker version)

### Database

Run the SQL from section 3 in HeidiSQL.

### Build and test

```bash
mvn clean package
```

### Run locally

```bash
mvn javafx:run
```

### Run as a Docker image

Start Xming with XLaunch: Multiple windows -> Start no client -> tick
No Access Control -> Finish.

```bash
docker build -t alii123x/temp-converter-fx .
docker run --rm -e DISPLAY=host.docker.internal:0.0 alii123x/temp-converter-fx
```

### Jenkins

The pipeline needs a Maven installation named `MAVEN_HOME` in Manage Jenkins ->
Tools, and a username/password credential with the ID `dockerhub` containing a
Docker Hub access token. Create a Pipeline job, select "Pipeline script from
SCM", point it to this repository, branch `master`, script path `Jenkinsfile`.
