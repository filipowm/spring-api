<div align="center">
  <h1>Spring API</h1>
  <br />
  <a href="#about"><strong>Explore the docs »</strong></a>
  <br />
  <br />
  <a href="https://github.com/filipowm/spring-api/issues/new?assignees=&labels=bug&template=01_BUG_REPORT.md&title=bug%3A+">Report a Bug</a>
  ·
  <a href="https://github.com/filipowm/spring-api/issues/new?assignees=&labels=enhancement&template=02_FEATURE_REQUEST.md&title=feat%3A+">Request a Feature</a>
  ·
  <a href="https://github.com/filipowm/spring-api/discussions">Ask a Question</a>
</div>

<div align="center">
<br />

[![CI](https://img.shields.io/github/workflow/status/filipowm/spring-api/CI-CD/main?style=flat-square)](https://github.com/filipowm/spring-api/actions/workflows/ci.yml)
[![Last Commit](https://img.shields.io/github/last-commit/filipowm/spring-api/main?style=flat-square)](https://github.com/filipowm/spring-api/commits/main)
[![Commit activity](https://img.shields.io/github/commit-activity/m/filipowm/spring-api?style=flat-square)](https://github.com/filipowm/spring-api/pulse)
[![Project license](https://img.shields.io/github/license/filipowm/spring-api.svg?style=flat-square)](LICENSE)

[![Pull Requests welcome](https://img.shields.io/badge/PRs-welcome-bc36f0.svg?style=flat-square)](https://github.com/filipowm/spring-api/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)

</div>

<details open="open">
<summary>Table of Contents</summary>

- [✨ About](#about)
  * [💡 Motivation](#motivation)
  * [🔥 Features](#features)
- [🚀 Getting Started](#getting-started)
  * [🔧 Configuration Properties](#configuration-properties)
- [Future plans](#plans)

</details>

## <a id="about"></a> ✨ About

Spring API is a library that aims for simplifying code for managing 
REST API written with Spring Framework. It helps to stick to certain
conventions and good practices, simplifies API versioning.

### <a id="motivation"></a> 💡 Motivation

A lot of projects have challenges in unifying
approach to building API, especially those which are complex
or based on microservices. Goal of this library is to:
- get rid of boilerplate code and magic strings in endpoints' declarations
- simplify and unify how API is versioned
- unify overall API declaration and endpoint paths

### <a id="features"></a> 🔥 Features

- apply prefix to all endpoints
- version API in path or content type 
- configure base API context (useful especially in microservices)
- provide default endpoint names when not set explicitly
- handy utilities removing boilerplate code used to define endpoints with Spring
- support both "standard" Servlet-based API as well as Reactive API 
- support OpenAPI 3.0 generation using [SpringDoc](https://springdoc.org/) library

## <a id="getting-started"></a> 🚀 Getting Started

This library works with Java 17 and Spring Boot 3 / Spring Framework 6.
It is preferred to use `spring-api-starter` with Spring Boot.

When starter is used, it automatically detects whether you are using `webmvc` (servlet approach)
or `webflux` (reactive approach). Additionally, it automatically configure SpringDoc OpenAPI generation
when it detects SpringDoc is being used.

1. Add dependency:
    - Maven:
      ```xml
        <dependency>
            <groupId>io.github.filipowm</groupId>
            <artifactId>spring-api-starter</artifactId>
            <version>2.0.0</version>
        </dependency>
      ```
    - Gradle:
      ```groovy
      implementation 'io.github.filipowm:spring-api-starter:2.0.0'
      ```

2. To customize library configuration, check [🔧 Configuration Properties](#configuration-properties) section.

3. Use `@Api`, `@ApiVersion` annotations to leverage library features. You can
   still use Spring annotations and mix controllers annotated with `@Api` and
   `@RestController`. `@Api` is a replacement for both `@RestController` and
   `@RequestMapping` and enables this library features.

To define a version of endpoints within controller you can use either:
```java
@Api(value = "/hello", version = @ApiVersion(2))
```
or
```java
@Api(value = "/hello")
@ApiVersion(2)
```

**Examples:**  

These examples use default Spring API configuration.

- following snippet would generate `GET /api/v1/hello` endpoint returning `200 OK` status
    ```java
    @Api
    class HelloController {
        @GetMapping
        String get() { 
            return "Hello World";
        }
    }
    ```

- following snippet would generate `POST /api/v2/my-awesome-api/hi` endpoint returning `201 Accepted` status  
    ```java
    @Api("/my-awesome-api")
    @ApiVersion(2)
    class HelloController {
        @Accepted
        @PostMapping("hi")
        String get() {
            return "Hello World";
        }
    }
    ```

### <a id="configuration-properties"></a> 🔧 Configuration properties

Configuration properties use standard Spring Boot approach to defining properties.

| **Property**                                 | **Description**                                                                                                                                                                                                                                                                                 | **Default value** |
|----------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| `spring.api.pathPrefix`                      | Prefix applied to all endpoints using `@Api` annotation, e.g. when set to `/rest/` resulting path may be `/rest/v1/hello`.                                                                                                                                                                      | `/api`            |
| `spring.api.baseContext`                     | Context used after prefix and version in path. It can be used to make endpoints unique between microservices, or just use it to fulfill any other needs, e.g. when set to `billing`, all path may look like `/api/v1/billing/hello`.                                                            | ``                |
| `spring.api.versioning.enabled`              | Flag whether API versioning should be enabled. When enabled, you can version API using `@ApiVersion` annotation. Otherwise using it does not take any effect and paths may look like `/api/hello` instead of `/api/v3/hello`.                                                                   | `true`            |
| `spring.api.versioning.versionPrefix`        | Prefix applied to API version number, e.g. if set to `ver` resulting path may be `/api/ver1/hello` or content type: `application/ver1+json` when content type versioning is used.                                                                                                               | `v`               |
| `spring.api.versioning.versionInContentType` | Flag whether API should be versioned using `Content-Type` HTTP header. When enabled, API is versioned using `Content-Type` header instead of URI path.                                                                                                                                          | `false`           |
| `spring.api.versioning.contentTypeVnd`       | Definition of vendor-specific MIME type applied to `Content-Type` HTTP header, e.g. when set to `vnd.app` resulting content type may be `application/vnd.app.v1+json`, depending on defined `consumes` property in `@Api` annotation. When empty, resulting content type may be `text/v1+html`. | ``                |

**Example:**

```yaml
spring:
  api:
    pathPrefix: "/rest"
    baseContext: "/warehouse"
    versioning:
      enabled: true
      versionPrefix: v
      versionInContentType: false
      contentTypeVnd:
```
Using this configuration endpoints may look like `/rest/v2/warehouse/hello`.

## <a id="plans"></a> Future plans

1. Detect endpoints collisions and block application from starting.
1. Allow using `contentTypeVnd` independently of content type versioning.

If you see a need to other features, please create an issue.
