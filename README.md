# Social Media Blog API

A RESTful backend for a micro-blogging platform built with Spring Boot. It manages
user accounts and messages, exposing endpoints for registration, login, and full
message CRUD.

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
