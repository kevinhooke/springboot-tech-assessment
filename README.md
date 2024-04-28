# SpringBoot Tech Assessment

## Implementation Stack
- Java 17
- Spring Boot 3.3.0
- spring-boot-starter-data-jpa - for JPA to persist request logging to relational db table
- h2 database - for runtime/inmemory database to demonstrate request logging to relational table using Spring Data JPA
- spring-boot-starter-validation - to provide Bean Validation annotations for validating EntryFile data
- Junit 4
- WireMock Standalone 3.3.1 (Standalone version to avoid WireMock's current Jetty 11 dependendency that is not provided
with SpringBoot 3.x
- wiremock-spring-boot 2.0.0 (For SpringBoot annotations for integating with WireMock)
- LogBack Classic - provides logging api
- Maven 3.x - for dependencies and build

## App package structure

SpringBoot application: kh.springbootassessment.fileparser.SpringBootTechnicalAssessmentApplication

src/main/java:

- kh.springbootassessment.fileparser.controller : RestController providing REST apis
- kh.springbootassessment.fileparser.data : JPA enties and other POJOs
- kh.springbootassessment.fileparser.repository - JPA Respositories providing data access
- kh.springbootassessment.fileparser.service - Spring Services - provides main endpoint logic
- kh.springbootassessment.fileparser.validation - data validators

src/main/resources:
- application.propperties to support externalized/configurable properties

src/test/java:
- 

src/test/resources:
- application.properties - test properties to support junits
