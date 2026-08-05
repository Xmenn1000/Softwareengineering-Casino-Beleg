# Design Decisions – Global

This file collects the decisions that apply to the whole project. Anything that's only about a single
service lives in that service's own `DESIGN_DECISIONS_<SERVICE>.md`.

## Decisions

We kicked things off by reading the assignment properly and turning it into one big UML diagram. That
made it pretty obvious which service has to talk to which, and honestly most of the decisions below just
grew out of that.

### 1. Transactions are seen from the game services' side

The bank's transaction endpoints always want a known game service as the invoicing party (we pin that
down with a `GameService` enum). So we treated transactions as a game-service thing from the start: a
transaction is always money moving between a game service and a user.

One thing we did on purpose because of that: a user topping up their own balance doesn't create a
transaction. There's a single balance — a deposit just moves that number, and only money coming from a
game service ends up in the transaction ledger. So the ledger is really a "what did the games do" log,
not a record of deposits.

In the real world a deposit would obviously get logged somewhere too (a separate cash ledger, for
auditing and all that). But the assignment only asks for game-service transactions and never for deposit
tracking, so we left it out. Conscious simplification, not something we forgot.

We ran this past the lecturer in the exercise session and got the go-ahead.

### 2. Entities don't get their own interface

We don't slap an interface on top of our JPA entities. It would only ever have one implementation, gives
us zero polymorphism, and JPA code would keep casting back to the concrete type anyway — so it's an
abstraction that pays for nothing. Classic **You Aren't Gonna Need It (YAGNI)** / **Keep It Simple, Stupid (KISS)** territory (*E-06
Designprinzipien*, lecture).

And no, it doesn't step on the **Dependency Inversion Principle** (*E-06 Designprinzipien*, lecture):
our services already depend on the `JpaRepository` abstraction, not on a concrete database — exactly the
`UserService → IDatabase` example from the lecture. The entity is just data, not a dependency worth
inverting.

If we really wanted to decouple the domain model, we'd have to go full hexagonal / clean architecture
(domain and entity as separate types with a mapper in between). But the architecture is set by the
assignment — direct JPA entities — so we didn't. We also checked this with the lecturer in the
exercises.

### 3. One way of doing errors, everywhere

Errors turn into HTTP responses the same way in every service: an abstract `HttpException` carries the
status, the concrete cases extend it, and a `@RestControllerAdvice` turns them into RFC-7807
`ProblemDetail` responses. Our actual code just throws a meaningful exception and never fiddles with
status codes — that's the advice layer's one job (**Separation of Concerns** and the **Single
Responsibility Principle**, *E-06 Designprinzipien*, lecture).

### 4. The Swagger stuff lives on its own interface

Each controller implements a little `...Api` interface that holds all the OpenAPI/Swagger annotations.
Keeps the controller itself readable and puts the whole API contract in one spot. Same setup in every
service.

### 5. Randomness sits behind an abstraction so we can test it

We don't call a random source straight from the game logic. Roulette gets its number from a
`RouletteSpinGenerator`, and the slot reels get their `Random` passed in through the constructor. That
way a test can hand in a fixed source and check for an exact result — which is basically the only reason
the game logic is testable at all. Straight-up **Dependency Inversion Principle** (*E-06
Designprinzipien*, lecture), and honestly the same thing we were taught in Programmierung 3.

### 6. Stats are done in memory with streams

We don't calculate the stats with DB queries. A little stateless calculator takes the list of games and
reduces it with streams. That keeps the whole thing a pure function we can test without a database or
mocks, and it works the same in both game services. Calculator does the maths, service does the wiring —
**Single Responsibility Principle** plus **High Cohesion / Low Coupling** (*E-06 Designprinzipien*, lecture).

### 7. Entities validate themselves when they're created

No bare constructor plus setters. Entities go through a factory / static `create(...)` that checks the
invariants, so a broken entity can't even exist. The model looks after itself no matter how careful (or
not) the caller is.

### 8. The API talks camelCase

All our DTOs use camelCase field names, so the JSON comes out camelCase (`totalClientCount`) instead of
the snake_case in the assignment examples. On purpose: it keeps the JSON matching our Java naming
everywhere and saves us a `@JsonProperty` on every single field. Yes, it's a deliberate deviation from
the example bodies.
