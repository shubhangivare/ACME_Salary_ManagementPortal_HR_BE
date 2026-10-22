# PeoplePay Spring Boot backend (refactored)

Java 17, Spring Boot 3.4.5, Spring Security, Spring Data JPA/Hibernate and H2.

## Layered architecture

- `controller/ApiController.java` — HTTP routes and request/response mapping only.
- `service/PeoplePayService.java` — business rules, validation beyond DTO constraints, transactions and audit writes.
- `repository/` — Spring Data JPA repositories; derived query methods replace handwritten SQL.
- `entity/` — annotated JPA entities mapped to `employees`, `countries`, `audit_events` and `salary_revisions`.
- `dto/` — validated API request records.
- `src/test/java/com/peoplepay/PeoplePayServiceTest.java` — unit tests for core service behavior.

## Run

Install JDK 17+ and Maven, then run:

```bash
mvn clean test
mvn spring-boot:run
```

Backend: `http://localhost:8080`. H2 console (development only): `http://localhost:8080/h2-console`, JDBC URL `jdbc:h2:mem:peoplepaydb`, user `sa`, blank password.

Development accounts: `admin` / `admin123` and `hr` / `hr123`. These are demo credentials only. Do not use them in production; use managed secrets, HTTPS and a production-grade authentication strategy.

## API endpoints

- `POST /api/auth/login`
- `GET /api/employees`, `GET /api/employees/{id}`
- `POST /api/employees`, `PUT /api/employees/{id}`, `PATCH /api/employees/{id}/deactivate`
- `POST /api/employees/import`
- `GET /api/audit`, `POST /api/audit`
- `GET /api/countries`, `PUT /api/countries/{code}`
- `GET /api/salary-revisions`, `POST /api/salary-revisions`

## Notes

The app loads `schema.sql` and `db.sql` into an in-memory H2 database at startup. Data resets when the app restarts. Hibernate schema validation is enabled so entity mappings are checked against the SQL schema. Employee list retrieval uses a repository-derived ordered query; the audit feed is capped at 500 records. Service methods are transactional, including salary revision plus employee salary update.

Tests are included but were not executed in this environment because Maven could not be launched here. Run `mvn clean test` locally to verify.
