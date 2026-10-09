# OP1 - Ohjelmistotuotantoprojekti 1

![Java](https://img.shields.io/badge/Java-17%2B-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-GUI-green)
![H2](https://img.shields.io/badge/H2-Database-orange)
![Maven](https://img.shields.io/badge/Build-Maven-red)
![Jenkins](https://img.shields.io/badge/CI-Jenkins-lightgrey)
![Docker](https://img.shields.io/badge/Container-Docker-blue)

Course repository for **Ohjelmistotuotantoprojekti 1**. The project demonstrates a complete Java desktop application workflow with a JavaFX GUI, persistent H2 database storage, automated tests, code coverage reporting, Docker support, and a Jenkins CI pipeline.

## 1. Assignment Description

This repository contains the deliverables for Ohjelmistotuotantoprojekti 1. The project progresses through multiple assignments and culminates in Assignment 5, which delivers a full desktop application.

### Problem Statement

Build a Java desktop application that demonstrates a complete software production workflow:

- graphical user interface
- persistent data storage
- automated testing
- code coverage reporting
- containerized and CI-ready build

### Key Requirements

- JavaFX-based GUI for user interaction
- Persistent storage backed by an embedded H2 database
- Working temperature conversion functionality
- Unit tests covering core logic and database behavior
- JaCoCo code coverage reports
- Dockerfile for containerized execution
- Jenkinsfile for continuous integration

### Deliverables

| Deliverable | Location |
| --- | --- |
| Source code | `src/` |
| Documentation and coverage reports | `docs/` |
| Build configuration | `pom.xml` |
| Container definition | `Dockerfile` |
| CI pipeline | `Jenkinsfile` |

## 2. Technologies and Tools

| Category | Technology / Tool |
| --- | --- |
| Language | Java 17+ |
| GUI framework | JavaFX |
| Database | H2 embedded database |
| Build tool | Apache Maven |
| Testing framework | JUnit 5 |
| Code coverage | JaCoCo |
| CI/CD | Jenkins |
| Containerization | Docker |
| Version control | Git / GitHub |

## 3. Design Approach and Implementation

The application follows a layered structure that separates presentation, business logic, and persistence.

### 3.1 GUI Design

The user interface is built with JavaFX. Controllers and event handlers connect user input to service-layer functionality.

The interface focuses on temperature conversion so the demonstration remains clearly scoped while still exercising all major layers of the application.

### 3.2 Business Logic

Core conversion logic is isolated from the UI so it can be unit tested independently.

Temperature conversion formulas are implemented as deterministic logic, making them easy to verify with automated tests.

### 3.3 Database Design

The application uses an embedded H2 database for local persistence.

Database access is encapsulated in DAO classes so the GUI does not interact with SQL directly.

### 3.4 Key Decisions

- **Separation of concerns:** UI, logic, and persistence are decoupled to improve testability and maintainability.
- **Embedded database:** H2 keeps the project self-contained and easy to run without an external database server.
- **Automated coverage:** JaCoCo is integrated into the Maven build so coverage reports are generated during testing.
- **CI and container support:** Docker and Jenkins demonstrate a production-style build workflow.

## 4. Testing and Quality Assurance

### 4.1 Automated Testing

- Testing framework: JUnit 5
- Coverage tool: JaCoCo
- Coverage report path: `target/site/jacoco/index.html`

### 4.2 Test Cases and Results

| # | Test Scenario | Expected Result | Outcome |
| --- | --- | --- | --- |
| 1 | Convert `0 C` to Fahrenheit | `32 F` | Pass |
| 2 | Convert `100 C` to Fahrenheit | `212 F` | Pass |
| 3 | Convert `-40 C` to Fahrenheit | `-40 F` | Pass |
| 4 | Convert `32 F` to Celsius | `0 C` | Pass |
| 5 | Round-trip conversion consistency | Original value restored | Pass |
| 6 | Database insert and retrieval | Record persisted and read back correctly | Pass |

### 4.3 Manual Verification

- Launched the JavaFX GUI and tested the input, conversion, and display flow.
- Verified that data written to H2 persists across application restarts.
- Confirmed that the JaCoCo HTML report renders correctly.
- Built and ran the Docker image to confirm the app starts in a clean environment.

### 4.4 CI Verification

The `Jenkinsfile` defines a pipeline that:

1. Checks out the repository.
2. Builds the project with Maven.
3. Runs automated tests.
4. Publishes JaCoCo coverage results.

## 5. How to Run

### Prerequisites

- JDK 17 or newer
- Apache Maven 3.8+
- Docker, optional for containerized execution
- Desktop environment capable of running JavaFX

### 5.1 Build and Test Locally

```bash
# Clone the repository
git clone https://github.com/Moikk4/OP1.git
cd OP1

# Compile and run tests
mvn clean test

# Package the application
mvn clean package
```

### 5.2 Run the JavaFX Application

After packaging, run the generated JAR:

```bash
java -jar target/op1-1.0-SNAPSHOT.jar
```

### 5.3 View Coverage Report

After running `mvn test`, open:

```text
target/site/jacoco/index.html
```

### 5.4 Run with Docker

```bash
# Build the image
docker build -t op1-app .

# Run the container
docker run --rm op1-app
```

## Repository Structure

```text
OP1/
|-- docs/                  # Documentation and coverage reports
|-- src/                   # Application source code and unit tests
|-- Dockerfile             # Container build definition
|-- Jenkinsfile            # CI pipeline definition
|-- pom.xml                # Maven project configuration
`-- README.md              # Project documentation
```

## Project Information

- Course: Ohjelmistotuotantoprojekti 1
- Repository: `Moikk4/OP1`
