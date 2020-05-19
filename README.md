# Score card application

Minimal [Spring Boot](http://projects.spring.io/spring-boot/) sample app.

## Requirements

For building and running the application you need:

- [JDK 11.x](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
- [Gradle 6.x](https://gradle.org/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.pipa.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
$./gradlew bootJar (for linux)

gradlew.bat bootJar (for windows)
```

## Swagger 
Once the application is successfully running the rest controllers can be accessed from

```shell
http://localhost:9000/swagger-ui.html#/
```
