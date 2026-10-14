# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

- Spring Boot upgraded from 2.5.5 to 4.1.1
- Java baseline upgraded from 11 to 25
- Migrated persistence annotations from `javax.persistence` to `jakarta.persistence` (`Account`, `Message`)
- H2 database now managed by Spring Boot (2.4.240) instead of a pinned version
- `spring-boot-maven-plugin` version now managed by the Spring Boot parent
- Compiler configuration now driven by the `java.version` property
- Removed unused imports in `SocialMediaApp` (`RestTemplateBuilder` relocated to the `spring-boot-restclient` module in Spring Boot 4)

### Added

- Test-scoped `jackson-databind` 2.22.3 dependency: Spring Boot 4 ships Jackson 3 (`tools.jackson`), while the provided test suite imports Jackson 2 (`com.fasterxml.jackson.databind`)

## [1.1] - 2026-10-13

### Security

- H2 database bumped from 2.1.214 to 2.2.220 via Dependabot (CVE-2022-45868), PR #1

## [1.0] - 2023-09-26

### Added

- Account registration and login endpoints (`POST /register`, `POST /login`)
- Message CRUD endpoints (`POST`, `GET`, `PATCH`, `DELETE /messages`, `GET /accounts/{account_id}/messages`)
- Spring Data JPA repositories for `Account` and `Message` entities backed by an in-memory H2 database
