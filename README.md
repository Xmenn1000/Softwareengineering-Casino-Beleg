# Softwareengineering-Casino-Beleg

The goal of this project is to build a RESTful microservice architecture as a group project. The application models an online casino where users can create accounts via the provided APIs and play the available games risk-free.

## Conventions
Commit messages:
**Format:** `<type>(<scope>): <description>`

**Types:**
- `feat` – new feature
- `fix` – bug fix
- `chore` – tooling, dependencies, build config
- `refactor` – code change that neither fixes a bug nor adds a feature
- `docs` – documentation only
- `test` – adding or adjusting tests
- `style` – formatting, no code change

**Scopes:** `banking`, `roulette`, `slots`, `root` (repo-wide)

**Examples:**
```
feat(banking): add withdrawal endpoint
fix(roulette): correct payout for split bets
refactor(banking, slots): extract SpinHandler
chore(root): update docker-compose
```

## Getting started

1. Open the project in the root folder (`Softwareengineering-Casino-Beleg`).
2. Pick one of the setups below depending on what you need:

**Everything in Docker (3 services + 3 databases):**
```bash
docker compose --profile full up --build
```

**Same thing, but detached (runs in the background):**
```bash
docker compose --profile full up -d --build
```

**Only the databases:**
```bash
docker compose --profile db up --build
```

**Only the databases, but detached (runs in the background):**
```bash
docker compose --profile db up -d --build
```

If you just start the databases, you can run the services from IntelliJ. Handy for debugging.
If you want to go this way, you need to add the `banking`, `roulette`, and `slots` folders as modules in the Project Structure settings.

## Stopping things

```bash
docker compose --profile full down
```

If you also want to wipe the database data, add `-v`:
```bash
docker compose --profile full down -v
```

## Important Links

### Services

| Service  | Swagger UI                                          | OpenAPI Spec                            | 
|----------|-----------------------------------------------------|-----------------------------------------|
| Banking  | http://localhost:8080/swagger-ui/index.html         | http://localhost:8080/v3/api-docs       | 
| Roulette | http://localhost:8081/swagger-ui/index.html         | http://localhost:8081/v3/api-docs       |
| Slots    | http://localhost:8082/swagger-ui/index.html         | http://localhost:8082/v3/api-docs       | 

### Databases

| Service  | Host:Port         | DB Name  | User         | Password     |
|----------|-------------------|----------|--------------|--------------|
| Banking  | localhost:55432   | banking  | postgresUser | postgresPW   |
| Roulette | localhost:55433   | roulette | postgresUser | postgresPW   |
| Slots    | localhost:55434   | slots    | postgresUser | postgresPW   |