# Softwareengineering-Casino-Beleg

The goal of this project is to build a RESTful microservice architecture as a group project. The application models an online casino where users can create accounts via the provided APIs and play the available games risk-free.

# Autoren
- Ryu Siegert
- Gordian Reinhold
- Nils Scharein 

## Design Decisions

All design decisions are documented in dedicated Markdown files. There is a global one in the project
root and a service-specific one inside each service in which decisions were made.

- [`DESIGN_DECISIONS_GLOBAL.md`](DESIGN_DECISIONS_GLOBAL.md) in the root holds the global decisions
  that apply to the whole project.
- Each service has its own file — [`banking/DESIGN_DECISIONS_BANKING.md`](banking/DESIGN_DECISIONS_BANKING.md),
  [`roulette/rouletteServiceREADME.md`](roulette/DESIGN_DECISIONS_ROULETTE.md),
  [`slots/DESIGN_DECISIONS_SLOTS.md`](slots/DESIGN_DECISIONS_SLOTS.md) — holding the decisions that
  apply only to that specific service.

# KI-Nutzung im Projekt

Im Rahmen dieses Projekts haben wir KI-Werkzeuge (u.a. LLM-basierte Assistenten)
unterstützend eingesetzt. Der Einsatz beschränkte sich im Wesentlichen auf:

- **Code-Review:** Prüfen von Klassen und Methoden auf Verständlichkeit, mögliche
  Fehlerquellen und Einhaltung der SOLID-Prinzipien.
- **Verbesserung der Code-Qualität:** Vorschläge zu Refactorings, Benennungen,
  Vereinfachungen und dem Abbau von Redundanz.
- **Unterstützung bei Tests und Dokumentation:** Anregungen für Testfälle und
  sprachliche Überarbeitung der Dokumentation.

Der fachliche Entwurf, die Architekturentscheidungen und die finale Umsetzung
stammen von uns. Alle von der KI stammenden Vorschläge wurden vor der Übernahme
geprüft, angepasst und getestet. Die Verantwortung für den abgegebenen Code liegt
vollständig bei den Autoren.

## Prerequisites
- Docker Desktop (Windows/macOS) or Docker Engine + Docker Compose (Linux)
- Java 21 (only required if you want to run services from IntelliJ)
- Maven 3.9+ (only required if you want to build outside Docker)
- IntelliJ IDEA (recommended for local development)

## Lizenz

Dieses Projekt steht unter der **GNU General Public License v3.0 (GPL-3.0)**.
Der vollständige Lizenztext befindet sich in der gesonderten Datei [`LICENSE`](LICENSE).

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

### Common startup errors

```bash
unable to get image 'postgres:17': error during connect: Get "http://%2F%2F.%2Fpipe%2FdockerDesktopLinuxEngine/v1.48/images/postgres:17/json": open //./pipe/dockerDesktopLinuxEngine: The system cannot find the file specified.
```

This just means you forgot to start Docker Desktop first.

## Important Links

[Swagger Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)

### Services

| Service  | Swagger UI                                          | OpenAPI Spec                            | 
|----------|-----------------------------------------------------|-----------------------------------------|
| Banking  | http://localhost:8080/swagger-ui/index.html         | http://localhost:8080/v3/api-docs       | 
| Roulette | http://localhost:8081/swagger-ui/index.html         | http://localhost:8081/v3/api-docs       |
| Slots    | http://localhost:8082/swagger-ui/index.html         | http://localhost:8082/v3/api-docs       | 


## See Databases in IntelliJ IDEA

After starting the databases as described in [Getting started](#getting-started), you can connect IntelliJ IDEA directly to the local PostgreSQL databases.

### Open the Database tool window

In IntelliJ IDEA, open:

```text
View > Tool Windows > Database
```

Then create a new PostgreSQL data source:

```text
+ > Data Source > PostgreSQL
```

### Connection settings

| Service  | Host      | Port  | Database | User         | Password   | JDBC URL                                   |
|----------|-----------|-------|----------|--------------|------------|--------------------------------------------|
| Banking  | localhost | 15432 | banking  | postgresUser | postgresPW | jdbc:postgresql://localhost:15432/banking  |
| Roulette | localhost | 15433 | roulette | postgresUser | postgresPW | jdbc:postgresql://localhost:15433/roulette |
| Slots    | localhost | 15434 | slots    | postgresUser | postgresPW | jdbc:postgresql://localhost:15434/slots    |

Click **Test Connection**.

If IntelliJ asks to download the PostgreSQL driver, confirm it.

Then click **OK** or **Apply**.

### Using copied IntelliJ data source settings

If someone gives you copied IntelliJ database settings, you can paste them directly into IntelliJ.

1. Open the **Database** tool window.
2. Click inside the database panel.
3. Paste the copied data source settings with `Ctrl + V`.
4. IntelliJ should create the data source automatically.
5. Check the password field, because passwords are often not included when data sources are copied.
6. Click **Test Connection** to verify that the connection works.

#### Banking DB Connection 
#DataSourceSettings#
#LocalDataSource: banking@localhost
#BEGIN#
<data-source source="LOCAL" name="banking@localhost" uuid="91e74f3f-06d9-4dd0-9dec-9528afa2bc1b"><database-info product="PostgreSQL" version="17.10 (Debian 17.10-1.pgdg13+1)" jdbc-version="4.2" driver-name="PostgreSQL JDBC Driver" driver-version="42.7.3" dbms="POSTGRES" exact-version="17.10" exact-driver-version="42.7"><identifier-quote-string>&quot;</identifier-quote-string></database-info><case-sensitivity plain-identifiers="lower" quoted-identifiers="exact"/><driver-ref>postgresql</driver-ref><synchronize>true</synchronize><jdbc-driver>org.postgresql.Driver</jdbc-driver><jdbc-url>jdbc:postgresql://localhost:15432/banking</jdbc-url><jdbc-additional-properties><property name="com.intellij.clouds.kubernetes.db.host.port"/><property name="com.intellij.clouds.kubernetes.db.enabled" value="false"/><property name="com.intellij.clouds.kubernetes.db.container.port"/></jdbc-additional-properties><secret-storage>master_key</secret-storage><user-name>postgresUser</user-name><schema-mapping><introspection-scope><node kind="database" qname="@"><node kind="schema" qname="@"/></node></introspection-scope></schema-mapping><working-dir>$ProjectFileDir$</working-dir></data-source>
#END#

#### Roulette DB Connection
#DataSourceSettings#
#LocalDataSource: roulette@localhost
#BEGIN#
<data-source source="LOCAL" name="roulette@localhost" uuid="74130337-8014-4eb5-b8a3-4a0729fbf7d7"><database-info product="PostgreSQL" version="17.10 (Debian 17.10-1.pgdg13+1)" jdbc-version="4.2" driver-name="PostgreSQL JDBC Driver" driver-version="42.7.3" dbms="POSTGRES" exact-version="17.10" exact-driver-version="42.7"><identifier-quote-string>&quot;</identifier-quote-string></database-info><case-sensitivity plain-identifiers="lower" quoted-identifiers="exact"/><driver-ref>postgresql</driver-ref><synchronize>true</synchronize><jdbc-driver>org.postgresql.Driver</jdbc-driver><jdbc-url>jdbc:postgresql://localhost:15433/roulette</jdbc-url><jdbc-additional-properties><property name="com.intellij.clouds.kubernetes.db.host.port"/><property name="com.intellij.clouds.kubernetes.db.enabled" value="false"/><property name="com.intellij.clouds.kubernetes.db.container.port"/></jdbc-additional-properties><secret-storage>master_key</secret-storage><user-name>postgresUser</user-name><schema-mapping><introspection-scope><node kind="database" qname="@"><node kind="schema" qname="@"/></node></introspection-scope></schema-mapping><working-dir>$ProjectFileDir$</working-dir></data-source>
#END#

#### Slots DB Connection
#DataSourceSettings#
#LocalDataSource: slots@localhost
#BEGIN#
<data-source source="LOCAL" name="slots@localhost " uuid="ae30f66b-c6df-4881-ae5a-9c6c87a33799"><database-info product="PostgreSQL" version="17.10 (Debian 17.10-1.pgdg13+1)" jdbc-version="4.2" driver-name="PostgreSQL JDBC Driver" driver-version="42.7.3" dbms="POSTGRES" exact-version="17.10" exact-driver-version="42.7"><identifier-quote-string>&quot;</identifier-quote-string></database-info><case-sensitivity plain-identifiers="lower" quoted-identifiers="exact"/><driver-ref>postgresql</driver-ref><synchronize>true</synchronize><jdbc-driver>org.postgresql.Driver</jdbc-driver><jdbc-url>jdbc:postgresql://localhost:15434/slots</jdbc-url><jdbc-additional-properties><property name="com.intellij.clouds.kubernetes.db.host.port"/><property name="com.intellij.clouds.kubernetes.db.enabled" value="false"/><property name="com.intellij.clouds.kubernetes.db.container.port"/></jdbc-additional-properties><secret-storage>master_key</secret-storage><user-name>postgresUser</user-name><schema-mapping><introspection-scope><node kind="database" qname="@"><node kind="schema" qname="@"/></node></introspection-scope></schema-mapping><working-dir>$ProjectFileDir$</working-dir></data-source>
#END#


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
refactor(root): extract shared SpinHandler
chore(root): update docker-compose
```

## Lizenz

Dieses Projekt steht unter der **GNU General Public License v3.0 (GPL-3.0)**.
Der vollständige Lizenztext befindet sich in der gesonderten Datei [`LICENSE`](LICENSE).
