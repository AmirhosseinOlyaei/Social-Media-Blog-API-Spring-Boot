# Social Media Blog API

A RESTful backend for a micro-blogging platform built with Spring Boot. It manages
user accounts and messages, exposing endpoints for registration, login, and full
message CRUD.

## Table of Contents

- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [API Reference](#api-reference)
- [Database Schema](#database-schema)
- [H2 Console](#h2-console)
- [Project Structure](#project-structure)
- [Changelog](#changelog)
- [Appendix: Original Project Brief](#appendix-original-project-brief)
  - [Background](#background)
  - [Spring Technical Requirement](#spring-technical-requirement)
  - [User Stories](#user-stories)

## Tech Stack

- **Java 25**
- **Spring Boot 4.1.1** (Spring MVC, Spring Data JPA, Hibernate 7)
- **H2** in-memory database (2.4.x)
- **Maven 3.10+**
- JUnit 5 integration tests (raw HTTP client, no mocks)

## Getting Started

### Prerequisites

- JDK 25 (JDK 17+ minimum)
- Maven 3.6.3+

### Build

```bash
mvn clean install
```

### Run

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. The database is seeded from
`src/main/resources/data.sql` on every boot and resets on restart.

### Test

```bash
mvn test
```

The suite starts the full application on port 8080 per test class, so stop any
running instance first.

## API Reference

| Method | Endpoint                  | Description            | Success                               | Error                                     |
| ------ | ------------------------- | ---------------------- | ------------------------------------- | ----------------------------------------- |
| POST   | `/register`               | Register an account    | 200 + account JSON                    | 400 invalid input, 409 duplicate username |
| POST   | `/login`                  | Verify credentials     | 200 + account JSON                    | 401 unauthorized                          |
| POST   | `/messages`               | Create a message       | 200 + message JSON                    | 400 invalid input                         |
| GET    | `/messages`               | List all messages      | 200 + JSON array (may be empty)       | -                                         |
| GET    | `/messages/{id}`          | Get a message by ID    | 200 + message JSON (empty if missing) | -                                         |
| PATCH  | `/messages/{id}`          | Update message text    | 200 + rows updated (`1`)              | 400 invalid input                         |
| DELETE | `/messages/{id}`          | Delete a message       | 200 + `1` if deleted, empty otherwise | -                                         |
| GET    | `/accounts/{id}/messages` | List a user's messages | 200 + JSON array (may be empty)       | -                                         |

### Validation Rules

- **Registration:** username must not be blank, password must be at least 4
  characters, username must be unique.
- **Message creation:** `message_text` must not be blank and must be 255
  characters or fewer; `posted_by` must reference an existing account.
- **Message update:** the message must exist; new `message_text` must not be
  blank and must be 255 characters or fewer.

### Examples

```bash
# Register
curl -X POST localhost:8080/register \
  -H "Content-Type: application/json" \
  -d '{"username": "alice", "password": "pass1234"}'

# Create a message
curl -X POST localhost:8080/messages \
  -H "Content-Type: application/json" \
  -d '{"posted_by": 9999, "message_text": "hello world", "time_posted_epoch": 1728946800}'

# List all messages
curl localhost:8080/messages
```

## Database Schema

Seeded automatically at startup (in-memory H2).

**Account**

```sql
account_id integer primary key auto_increment,
username   varchar(255) not null unique,
password   varchar(255)
```

**Message**

```sql
message_id        integer primary key auto_increment,
posted_by         integer,
message_text      varchar(255),
time_posted_epoch bigint,
foreign key (posted_by) references account(account_id)
```

## H2 Console

Available at `http://localhost:8080/h2-console` while the app is running:

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Project Structure

```
src/main/java/com/example/
├── SocialMediaApp.java        # Spring Boot entry point
├── controller/                # REST endpoints
├── service/                   # Business logic and validation
├── repository/                # Spring Data JPA repositories
└── entity/                    # Account and Message JPA entities
src/test/java/com/example/     # HTTP-level integration tests
```

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

---

# Appendix: Original Project Brief

## Background

Full-stack applications are typically concerned with both a front end, that displays information to the user and takes in input, and a backend, that manages persisted information.

This project is a backend for a social media app, which manages users' accounts as well as any messages that they submit to the application. The functionality leverages the Spring framework, which allows for automatic injection and configuration of many features, including data persistence, endpoints and conventional data manipulation logic (CRUD operations).

In this micro-blogging or messaging app, any user should be able to see all of the messages posted to the site, or they can see the messages posted by a particular user. In either case, the backend is able to deliver the data needed to display this information as well as process actions like logins, registrations, message creations, message updates, and message deletions.

## Spring Technical Requirement

The project leverages the Spring Boot Framework.

In addition to functional test cases, `SpringTest` verifies that the project leverages the Spring framework, Spring Boot, Spring MVC, and Spring Data.
`SpringTest` verifies the following:

- That beans exist for the AccountService, MessageService, AccountRepository, MessageRepository, and SocialMediaController classes
- That AccountRepository and MessageRepository are working JPARepositories based on their corresponding Account and Message entities
- That the Spring Boot app leverages MVC by checking for Spring's default error message structure

## User Stories

### 1: The API processes new user registrations.

A user can create a new Account on the endpoint POST localhost:8080/register. The body contains a representation of a JSON Account, without an account_id.

- The registration is successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. If all these conditions are met, the response body contains a JSON of the Account, including its account_id. The response status is 200 OK, which is the default. The new account is persisted to the database.
- If the registration is not successful due to a duplicate username, the response status is 409. (Conflict)
- If the registration is not successful for some other reason, the response status is 400. (Client error)

### 2: The API processes user logins.

A user can verify a login on the endpoint POST localhost:8080/login. The request body contains a JSON representation of an Account.

- The login is successful if and only if the username and password provided in the request body JSON match a real account existing on the database. If successful, the response body contains a JSON of the account, including its account_id. The response status is 200 OK, which is the default.
- If the login is not successful, the response status is 401. (Unauthorized)

### 3: The API processes the creation of new messages.

A user can submit a new post on the endpoint POST localhost:8080/messages. The request body contains a JSON representation of a message, which is persisted to the database, without a message_id.

- The creation of the message is successful if and only if the message_text is not blank, is under 255 characters, and posted_by refers to a real, existing user. If successful, the response body contains a JSON of the message, including its message_id. The response status is 200, which is the default. The new message is persisted to the database.
- If the creation of the message is not successful, the response status is 400. (Client error)

### 4: The API retrieves all messages.

A user can submit a GET request on the endpoint GET localhost:8080/messages.

- The response body contains a JSON representation of a list containing all messages retrieved from the database. The list is empty if there are no messages. The response status is always 200, which is the default.

### 5: The API retrieves a message by its ID.

A user can submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.

- The response body contains a JSON representation of the message identified by the message_id. The response body is empty if there is no such message. The response status is always 200, which is the default.

### 6: The API deletes a message identified by a message ID.

A user can submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.

- The deletion of an existing message removes an existing message from the database. If the message existed, the response body contains the number of rows updated (1). The response status is 200, which is the default.
- If the message did not exist, the response status is 200, but the response body is empty. This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint respond with the same type of response.

### 7: The API updates a message text identified by a message ID.

A user can submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. The request body contains a new message_text value to replace the message identified by message_id. The request body is not guaranteed to contain any other information.

- The update of a message is successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. If the update is successful, the response body contains the number of rows updated (1), and the response status is 200, which is the default. The message existing on the database has the updated message_text.
- If the update of the message is not successful for any reason, the response status is 400. (Client error)

### 8: The API retrieves all messages written by a particular user.

A user can submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

- The response body contains a JSON representation of a list containing all messages posted by a particular user, retrieved from the database. The list is empty if there are no messages. The response status is always 200, which is the default.

### 9: The project utilizes the Spring Framework.

- The project leverages the Spring framework, including dependency injection, autowire functionality and Spring annotations.
