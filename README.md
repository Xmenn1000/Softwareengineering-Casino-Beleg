# Softwareengineering-Casino-Beleg

The goal of this project is to build a RESTful microservice architecture as a group project. The application models an online casino where users can create accounts via the provided APIs and play the available games risk-free.

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