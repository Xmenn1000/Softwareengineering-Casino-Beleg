# Design Decisions – Global

This file documents design decisions that apply to the **entire project** (all services).
Service-specific decisions are documented in the `DESIGN_DECISIONS_<SERVICE>.md` file inside each service.

## Decisions

Our overall approach was to follow the path of least resistance: at the start we scanned the
assignment thoroughly and translated the requirements into a single global UML diagram. That diagram
let us see clearly which service has to communicate with which, and it drove the decisions below.

### 1. Transactions are modelled from the game services' perspective

The banking transaction endpoints always require a *known game service* as the invoicing party
(enforced through a fixed `GameService` enum). We took this as the guiding constraint and modelled
transactions purely from the services' point of view: a transaction always represents money moving
between a game service and a user.

We therefore separate the *real money flow* (a user topping up their balance) from the *played money*
(the flow driven by the game services). A user has a single balance; a direct deposit changes that
balance but creates **no** transaction entry, whereas every money movement initiated by a game service
is recorded as a transaction. The transaction ledger thus tracks only game-service money movements,
never the user's own deposits.

We are aware that in a real system a user deposit would normally be recorded as a transaction as well
(typically in a separate cash/payments ledger, for auditing and reconciliation). The assignment,
however, only defines transaction endpoints tied to a game service and does not require deposits to be
tracked as transactions — so we deliberately did not model them that way. This keeps the architecture
aligned with the assignment's requirements, as a conscious and documented simplification.

This decision was discussed and agreed upon with the lecturer during the exercise session.

### 2. Repository entities do not get a separate interface

We do not put a separate interface on top of our JPA entities. Such an interface would have only one
implementation and no polymorphism to gain, and JPA-specific code would keep casting back to the
concrete type anyway — so it is just an unnecessary abstraction. That goes against **YAGNI** and
**KISS** (*E-06 Designprinzipien*, lecture).

This does **not** break the **Dependency Inversion Principle** (*E-06 Designprinzipien*, lecture): our
services already depend on the `JpaRepository` abstraction, not on a concrete database — just like the
`UserService → IDatabase` example from the lecture. The entity is plain data, not a dependency to
invert, so it needs no interface.

To decouple the domain model properly, we would actually have to use a hexagonal / clean architecture
(domain and entity as separate types joined by a mapper). Since the architecture is prescribed by the
assignment, however — direct use of the JPA entity framework — we did not use a hexagonal architecture
here.

This decision was discussed and agreed upon with the lecturer during the exercise session.

### 3. Consistent error handling via a typed exception hierarchy

Every service maps errors to HTTP status codes the same way: an abstract `HttpException` (carrying an
`HttpStatus`) is subclassed into typed domain exceptions, and a `@RestControllerAdvice` translates
them into RFC-7807 `ProblemDetail` responses. Business code only ever throws a meaningful domain
exception; the advice layer is the single place that turns it into an HTTP response. This keeps
controllers and services free of status-code handling. It follows **Separation of Concerns** and the
**Single Responsibility Principle** (*E-06 Designprinzipien*, lecture): the advice layer is solely
responsible for turning domain exceptions into HTTP responses.

### 4. OpenAPI/Swagger contract lives on a separate interface

Controllers implement a dedicated `...Api` interface that carries all OpenAPI/Swagger annotations
(operations, responses, parameters). The implementation class stays free of documentation noise, and
the API contract can be read in one place. This convention is applied across all services.

### 5. Non-determinism is isolated behind an abstraction (for testability)

The random element of each game is hidden behind an injectable abstraction rather than calling a
random source inline: the roulette spin is produced by a `RouletteSpinGenerator` interface, and the
slot reels receive their `Random` through the constructor. Tests can therefore inject a deterministic
source and assert exact outcomes, which is what makes the game logic unit-testable. This is an
application of the **Dependency Inversion Principle** (*E-06 Designprinzipien*, lecture): the game
logic depends on a random abstraction, not on a concrete random source.
This is also the approach we were taught in Programmierung 3.

### 6. Statistics are computed in-memory with stream-based calculators

Aggregated statistics are not computed with database queries but by dedicated, stateless calculator
components that receive a list of game entities and reduce it with streams. This keeps the aggregation
logic a pure function that can be unit-tested without a database or mocks, and keeps it consistent
across the game services. This follows the **Single Responsibility Principle** and **High Cohesion /
Low Coupling** (*E-06 Designprinzipien*, lecture): the calculator only aggregates, the service only
orchestrates.

### 7. Domain models validate themselves on creation

Entities are not created with a bare constructor and setters; they are built through a factory / static
`create(...)` path that validates the invariants, so an instance cannot come into existence in an
invalid state. This protects domain integrity independently of how careful the caller is.

### 8. The API uses camelCase JSON

All DTOs use camelCase field names, so the exposed JSON keys are camelCase (e.g. `totalClientCount`)
instead of the snake_case shown in the assignment. This keeps the JSON consistent with our Java naming
conventions across all services and avoids per-field `@JsonProperty` mappings. It is a deliberate
deviation from the assignment's example response bodies.

