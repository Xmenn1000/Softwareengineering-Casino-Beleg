# Roulette Service README

## Short Summary

The Roulette Service represents the European roulette game service in the casino project. It
provides REST endpoints that allow a player to play a roulette round, read the game rules and
winning chances, and retrieve global, user-specific, and game-specific statistics.

The service stores every played round in its own PostgreSQL database. For user validation and
account movements, it communicates with the Banking Service. Therefore, the Roulette Service does
not manage user accounts or balances itself. It focuses on its own domain: roulette rules, game
evaluation, game history, and roulette statistics.

## Basic Structure

The Roulette Service is less strictly organized according to Vertical Slice Architecture than the
Banking Service. This matches the assignment requirements: the Banking Service is sliced more
strongly by subdomains such as User, Transaction, and Stat, while the game services may use a
simpler layered structure.

The most important layers are:

- `controller`: REST interface to the outside.
- `service`: application logic and coordination of the other components.
- `game`: actual roulette game logic.
- `validation`: central request validation.
- `repository`: database access.
- `model`: persisted database entity.
- `view`: DTOs for REST responses.
- `request`: DTOs for REST requests.
- `mapper`: conversion from entity to DTO.
- `requestClients`: communication with other services, in this case Banking.
- `config`: external configuration and Spring beans.
- `exceptions`: custom domain errors with matching HTTP status codes.
- `game.strategy`: Strategy Pattern for the individual bet types.
- `util`: enum types such as `BetType`.

The rough flow for one game round is:

```text
Player
  -> RouletteController
  -> RouletteServiceImpl
  -> RouletteRequestValidator
  -> BankingRestClient: validate user
  -> RouletteEngine: evaluate game
  -> RouletteBetStrategyResolver: find matching bet strategy
  -> BankingRestClient: book win/loss
  -> RouletteGameRepository: store game
  -> RouletteGameMapper: entity to response DTO
  -> Player
```

## REST API

The REST API is defined in `RouletteApi` and implemented by `RouletteController`.

### `POST /casino/roulette/api/play`

Starts exactly one roulette round.

Request:

```json
{
  "user": 1,
  "betType": "COLOR",
  "betValue": "RED",
  "amount": 10.00
}
```

The service first checks whether the request is formally valid. Then it checks via the Banking
Service whether the user exists. After that, the roulette round is evaluated, the win or loss is
booked through the Banking Service, and the game is stored in the Roulette database.

### `GET /casino/roulette/api/info/rules`

Returns the supported roulette rules as text. This includes the game variant, the supported bet
types, and the behavior of the number `0`.

### `GET /casino/roulette/api/info/chances`

Returns hit probabilities, payouts, Return to Player, and House Edge.

### `GET /casino/roulette/api/stats`

Returns global roulette statistics, for example the number of different players, number of games,
total turnover, total payout, and house profit.

### `GET /casino/roulette/api/stats/user/{userId}`

Returns statistics for a specific user. The user ID is validated, and the user is checked through
the Banking Service. If the user does not exist, the service returns `404 Not Found`. If the user
exists but has not played any roulette games yet, the statistic values may be `0`.

### `GET /casino/roulette/api/stats/games`

Returns a list of all stored roulette games.

### `GET /casino/roulette/api/stat/{gameId}`

Returns a single stored game by its game ID. If the game ID does not exist, the service returns
`404 Not Found`.

### `DELETE /casino/roulette/api/stat/{gameId}`

Deletes a stored game by its game ID and returns the deleted game as a DTO. If the game ID does not
exist, the service returns `404 Not Found`.

## Important Classes and Methods

## Start Class

### `RouletteApplication`

This class starts the Spring Boot application. It contains the `main` method and uses
`SpringApplication.run(...)` to start the Spring application context, create beans, and make the
REST endpoints available.

## Controller Layer

### `RouletteApi`

`RouletteApi` is an interface that describes the REST endpoints. It contains annotations such as
`@PostMapping`, `@GetMapping`, `@DeleteMapping`, and `@RequestMapping`.

Why use an interface?

This separates the API signature from the concrete controller implementation. It provides one
central place where the endpoints offered by the Roulette Service are visible. This pattern also
fits the style of the other services, because it keeps controllers thinner.

Important methods:

- `play(...)`: describes the endpoint for a new game round.
- `getRules()`: describes the endpoint for game rules.
- `getChances()`: describes the endpoint for winning chances.
- `getStats()`: describes the endpoint for global statistics.
- `getUserStats(...)`: describes the endpoint for user-specific statistics.
- `getGames()`: describes the endpoint for all stored games.
- `getGame(...)`: describes the endpoint for one specific game.
- `deleteGame(...)`: describes the endpoint for deleting a game.

### `RouletteController`

`RouletteController` implements `RouletteApi`. It intentionally contains almost no own logic. Its
task is to receive HTTP requests and forward them to `RouletteService`.

Example:

```java
return ResponseEntity.ok(rouletteService.play(request));
```

The controller therefore does not decide how roulette works. This is important because controllers
can otherwise quickly become too large. The domain logic is located in the service and game layers
instead.

### `RouletteAdviceController`

`RouletteAdviceController` handles central errors for the Roulette Controller. It catches custom
`HttpException` classes and converts them into HTTP responses with `ProblemDetail`.

This means that not every controller method needs its own `try/catch`. Domain errors can be thrown
where they make sense, and later they are centrally translated into a REST response.

## Service Layer

### `RouletteService`

`RouletteService` is the interface for the application logic. It describes which operations the
service offers without defining how they are implemented internally.

Why use an interface?

An interface is not strictly necessary here, but it is useful because it clearly marks the boundary
between controller and service. It also fits the style of many Spring applications and makes later
tests or alternative implementations easier.

### `RouletteServiceImpl`

`RouletteServiceImpl` is the central coordination class. It connects controller, validation,
Banking client, game engine, repository, statistics calculation, and mapper.

Important method: `play(RoulettePlayRequestDTO request)`

Flow:

1. `rouletteRequestValidator.validatePlayRequest(request)`
Checks whether request, user ID, bet type, and bet amount are valid.

2. `bankingRestClient.findUserById(request.getUser())`
Checks through the Banking Service whether the user exists.

3. `rouletteEngine.play(request)`
Executes the actual roulette round.

4. `bankingRestClient.createRouletteTransaction(...)`
Books the win or loss through the Banking Service.

5. `rouletteGameRepository.save(game)`
Stores the played round in the Roulette database.

6. `RouletteGameMapper.toPlayResultDto(savedGame)`
Converts the entity into a response DTO.

Important method: `getUserStats(Long userId)`

This method validates the user ID and also checks through Banking whether the user exists. Then all
roulette games for this user are read from the database and evaluated by `RouletteStatsCalculator`.

Why is Banking checked here as well?

The endpoint refers to a specific user. Therefore, it is cleaner from a domain perspective to
distinguish between "user does not exist" and "user exists but has not played roulette yet".

### `RouletteInfoService`

This class creates the text responses for:

- game rules
- winning chances
- RTP
- House Edge

The class was extracted so that `RouletteServiceImpl` does not contain too much text-building
logic. The service should coordinate, but it should not assemble long information texts itself.

Important methods:

- `getRules()`: returns the textual description of the roulette rules.
- `getChances()`: returns probabilities, payouts, RTP, and House Edge.

### `RouletteStatsCalculator`

This class calculates statistics from stored `RouletteGameEntity` objects.

Important methods:

- `calculateStats(List<RouletteGameEntity> games)`
Calculates global statistics across all games.

- `calculateUserStats(Long userId, List<RouletteGameEntity> games)`
Calculates statistics for a specific user.

Important values:

- `totalClientCount`: number of different players.
- `totalGamesCount`: number of all games.
- `totalCashOut`: sum of all positive winnings paid out to players.
- `totalTurnover`: sum of all bet amounts.
- `totalClientProfit`: net result from the players' perspective.
- `totalProfit`: net result from the house perspective.

The class uses Java Streams because the statistic calculation is readable as a data flow: take a
list, extract values, filter, and sum.

## Validation Layer

### `RouletteRequestValidator`

This class bundles the basic request validation.

Important methods:

- `validatePlayRequest(RoulettePlayRequestDTO request)`
Checks the complete request for `/play`.

- `validateUserId(Long userId)`
Checks whether a user ID exists and is greater than `0`.

Why was this class extracted?

Previously, part of the validation was located in the service and part of it in the engine. This
created duplicated checks, for example for empty requests or empty user IDs. With the validator, it
is clearer that basic request validation happens in one central place.

Why is not every validation placed there?

Roulette-specific rules such as "COLOR must be RED or BLACK" remain in the strategy classes because
they are not only technical request validation, but actual game rules. Entity protection remains in
`RouletteGameEntity`, so an invalid entity cannot be created.

## Game Layer

### `RouletteEngine`

`RouletteEngine` executes one single roulette round.

Important method: `play(RoulettePlayRequestDTO request)`

Flow:

1. `spinGenerator.spin()`
Creates a random ball position between `0` and `36`.

2. `rouletteBetStrategyResolver.resolve(request.getBetType())`
Finds the matching bet strategy for the provided `BetType`.

3. `strategy.isWinning(...)`
Uses the concrete strategy to check whether the bet won.

4. `calculateAmount(...)`
Calculates the win or loss amount.

5. `RouletteGameEntity.create(...)`
Creates a persistable entity for the played round.

Why was validation removed from the engine?

The engine should focus as much as possible on game evaluation. Request validation belongs in the
validator, Banking checks belong in the service, and the entity protects its own data. This gives
each class a clearer responsibility.

### `RouletteSpinGenerator`

`RouletteSpinGenerator` is an interface for spinning the roulette wheel.

Important method:

- `spin()`: returns a number between `0` and `36`.

Why use an interface?

This keeps the engine independent from direct random number generation. For tests, a fake or fixed
spin generator can be used later, for example one that always returns `17`. This makes the game
evaluation testable.

### `RandomRouletteSpinGenerator`

This class implements `RouletteSpinGenerator` and creates a real random ball position.

The method:

```java
ThreadLocalRandom.current().nextInt(37)
```

returns values from `0` to `36`. The upper bound `37` is exclusive, so `36` is the highest possible
value.

### `RouletteBetStrategy`

`RouletteBetStrategy` is the interface for the Strategy Pattern. Every supported bet type
implements this interface and encapsulates its own win logic, payout, and probability.

Important methods:

- `betType()`
Defines which `BetType` the strategy is responsible for.

- `isWinning(String betValue, int ballPosition)`
Checks whether a concrete bet wins for a concrete ball position.

- `payoutMultiplier()`
Returns the payout multiplier.

- `winningOutcomes()`
Returns how many winning fields this bet type has.

- `hitProbability()`
Calculates the hit probability.

- `returnToPlayer()`
Calculates the theoretical Return to Player.

- `houseEdge()`
Calculates the house edge.

### `RouletteBetStrategyResolver`

The resolver collects all strategy beans and stores them in a map by `BetType`. When the engine
evaluates a game round, it asks the resolver for the matching strategy.

Because of this, the engine does not need a long `if` or `switch` chain for all bet types. New bet
types can later be added by implementing a new strategy class and registering it as a Spring bean.

### Concrete Strategy Classes

Supported bet types:

- `StraightNumberBetStrategy`: `STRAIGHT_NUMBER`, one single number from `0` to `36`, payout
`35:1`.
- `ColorBetStrategy`: `COLOR`, `RED` or `BLACK`, payout `1:1`.
- `ParityBetStrategy`: `PARITY`, `EVEN` or `ODD`, payout `1:1`.
- `RangeBetStrategy`: `RANGE`, `LOW` or `HIGH`, payout `1:1`.
- `DozenBetStrategy`: `DOZEN`, `FIRST`, `SECOND`, or `THIRD`, payout `2:1`.

Why European roulette?

European roulette uses the numbers `0` to `36`, so it has 37 fields. American roulette would have
an additional `00` field and therefore 38 fields. The European version is easier to model, more
natural in a German/European context, and has a clear House Edge of `1/37`.

Why is `0` important?

The number `0` makes simple bets such as red/black, even/odd, or low/high not completely fair. For
red/black, there are 18 winning fields and 18 losing fields plus `0`. This is why the house wins in
the long run.

Why is House Edge not subtracted separately from the result?

The House Edge is created automatically by the combination of:

- 37 possible fields
- normal payouts
- the number `0`

Example red/black:

- hit probability: `18/37`
- payout on win: `1:1`
- loss probability: `19/37`

In the long run, this creates a house advantage of `1/37`. If the House Edge were subtracted again,
it would be mathematically counted twice and therefore wrong.

Why is House Edge not stored in `application.yaml`?

The House Edge is not a freely configurable technical setting. It is a mathematical consequence of
the roulette rules. If it were stored in the config, the config could contain a value that does not
match the actual rules.

Only values that are real operational settings are configurable, for example:

- minimum bet
- maximum bet
- Banking Service URL
- service name for Banking transactions

The game mathematics remain in the strategy classes.

## Persistence Layer

### `RouletteGameEntity`

This entity represents one stored roulette round in the `roulette_games` database table.

Important fields:

- `id`: technical game ID.
- `user`: ID of the player.
- `winning`: whether the round was won.
- `amount`: net win or net loss from the player's perspective.
- `betAmount`: original bet amount.
- `betType`: type of the bet.
- `betValue`: concrete value of the bet.
- `ballPosition`: drawn roulette number.

Important method:

- `create(...)`: factory method for creating a valid entity.

Why use a factory method?

The entity has a protected no-args constructor for JPA. From a domain perspective, however, an
entity should not be created in a half-valid state. Therefore, new entities are created through
`create(...)`. Central entity rules are checked there before the object is created.

Why is `betValue` normalized?

`betValue` is stored with `trim().toUpperCase()`. This means inputs such as `" red "`, `"Red"`, and
`"RED"` are all stored consistently as `"RED"`. This makes database entries more consistent.

### `RouletteGameRepository`

The repository extends `JpaRepository<RouletteGameEntity, Long>`.

Spring Data JPA automatically provides methods such as:

- `findAll()`
- `findById(...)`
- `save(...)`
- `delete(...)`

Additionally, there is:

- `findByUser(Long user)`

This method is derived by Spring Data JPA from the method name. Spring understands this as: search
all `RouletteGameEntity` entries whose `user` field matches the provided value.

## Mapper and DTOs

### `RouletteGameMapper`

The mapper converts database entities into DTOs for the REST output.

Important methods:

- `toGameDto(RouletteGameEntity entity)`
- `toPlayResultDto(RouletteGameEntity entity)`

Why use a mapper?

The entity belongs to the database layer. The DTOs belong to the REST layer. The mapper separates
these layers, so the database structure does not have to be exposed directly as the API structure.

### Request DTO

### `RoulettePlayRequestDTO`

This DTO describes the JSON body for `/play`.

Fields:

- `user`
- `betType`
- `betValue`
- `amount`

camelCase is used intentionally because the professor confirmed that it is allowed. Therefore, no
`@JsonProperty` annotations are needed.

### Response DTOs in the `view` Package

The DTOs in the `view` package represent the JSON responses of the REST API. In the assignment,
"View" does not mean an HTML document, but the external representation of REST data.

Important DTOs:

- `RoulettePlayResultDTO`: result of a played round.
- `RouletteGameDTO`: stored game.
- `RouletteStatsDTO`: global statistics.
- `RouletteUserStatsDTO`: user-specific statistics.

## Banking Communication

### `BankingRestClient`

This class communicates with the Banking Service via HTTP.

Important methods:

- `findUserById(Long userId)`
Asks the Banking Service whether a user exists.

- `createRouletteTransaction(Long userId, BigDecimal amount)`
Creates a transaction for a win or loss at the Banking Service.

Why does this class exist?

The Roulette Service should not need to know how HTTP calls are built technically. This
responsibility is located in the client. As a result, `RouletteServiceImpl` remains easier to read.

### `BankingUserDTO`

DTO for the response from the Banking Service when loading a user.

### `BankingTransactionRequestDTO`

DTO for the request to the Banking Service when a roulette transaction is created.

The `amount` is the net result from the player's perspective:

- positive amount: the player wins money.
- negative amount: the player loses money.

## Configuration

### `RouletteConfig`

This class creates the `RestClient` for the Banking Service.

Important method:

- `bankRestClient(...)`

The base URL comes from `application.yaml`.

### `RouletteProperties`

This class binds values from `application.yaml` to a Java object.

Currently configurable:

- `casino.roulette.betting.minAmount`
- `casino.roulette.betting.maxAmount`
- `casino.roulette.banking.invoicingParty`

Why are these values in the config?

These values are operational settings. It is realistic that they may need to be changed later
without changing code.

Why are not all game rules in the config?

Game rules such as payouts, winning probabilities, or House Edge belong together from a domain
perspective. If single values were freely configurable, contradictory rules could quickly occur.
Therefore, these rules are bundled in the strategy classes.

## Exceptions

### `HttpException`

Base class for domain-specific HTTP errors. It contains an HTTP status and an error message.

### `BadRouletteRequestException`

Used when a request is invalid from a technical or domain perspective. Result: `400 Bad Request`.

Examples:

- empty request
- user ID less than or equal to `0`
- missing bet amount
- bet amount below the minimum
- invalid bet value

### `BankingUserNotFoundException`

Used when the Banking Service cannot find a user. Result: `404 Not Found`.

### `RouletteGameNotFoundException`

Used when a stored roulette game cannot be found. Result: `404 Not Found`.

## Important Architecture Decisions

### Why Layered Architecture Instead of Vertical Slice?

The Banking Service is more complex because it has several subdomains such as User, Transaction,
and Stat. Vertical Slice Architecture is useful there because each subdomain can have its own
controllers, handlers, and data access.

The Roulette Service is smaller and has a narrower domain. The main domain is the roulette game.
Therefore, a layered architecture is more understandable:

- controller for REST
- service for coordination
- engine for game logic
- repository for database access
- client for Banking

This also matches the assignment requirement that the game services may be structured less strictly
than the Banking Service.

### Why DTOs Instead of Returning Entities Directly?

Entities belong to the database. DTOs belong to the API. If entities were returned directly, the API
would be strongly coupled to the database structure. With DTOs, the API can remain stable even if
the internal entity changes.

### Why `BigDecimal` for Money?

Money should not be calculated with `double` or `float`, because these types can create rounding
errors. `BigDecimal` is better suited for monetary values and is also required by the assignment.

### Why `Long` for IDs?

The assignment requires Long IDs. In addition, `Long` is very common for database IDs in Spring/JPA
and provides a sufficiently large value range.

### Why Is the Win Stored as a Net Result?

The `amount` field describes the result from the player's perspective:

- loss: `-betAmount`
- win: `betAmount * payoutMultiplier`

The original bet remains stored separately in `betAmount`. This allows statistics to calculate both
win/loss and turnover cleanly.

### Why Is the Banking Transaction Created After Game Evaluation?

Only after the game has been evaluated is it clear whether the player has won or lost. After that,
the Roulette Service can send exactly this amount to Banking.

Current flow:

1. Does the user exist?
2. Evaluate the game.
3. Book win/loss.
4. Store the game.

### Why Is the User Not Stored in the Roulette Database?

The Roulette Service stores only the user ID. The actual user data belongs to the Banking Service.
This separates the responsibilities of the services:

- Banking manages users and accounts.
- Roulette manages roulette games and roulette statistics.

### Why Is Number Generation Extracted?

With `RouletteSpinGenerator`, the random number can later be replaced in tests. Without this
extraction, the engine would be hard to test because every game would be random.

### Why Strategy Pattern for Bet Types?

Previously, all bet types were located in one central rules class with several `if` checks. That was
still acceptable for the current scope, but this class would have grown larger when adding more
roulette bets.

With the Strategy Pattern, every bet type has its own class. The engine only needs to know that
there is a matching strategy. `RouletteBetStrategyResolver` decides which concrete strategy is used.

This improves:

- readability: every bet type is located in its own class.
- extensibility: new bet types need a new strategy, but no large engine change.
- testability: every bet type can be tested in isolation.
- assignment traceability: the pattern is clearly visible in the code.

### Why No Additional Bet Types?

Roulette provides many more bet types, for example split, street, corner, six line, or column. For
the assignment, the most important and understandable bet types were implemented:

- Straight Number
- Color
- Parity
- Range
- Dozen

This covers the basic principles of roulette without making the implementation unnecessarily large.

### Why No `@JsonProperty` Annotations?

After consulting the professor, camelCase is allowed. Therefore, JSON fields such as `betType`,
`betValue`, or `ballPosition` do not need to be mapped to snake_case.

### Why `ProblemDetail` for Errors?

`ProblemDetail` is a standardized way of returning errors in REST APIs. This makes error responses
more consistent and includes the HTTP status together with the message.

## Database

The Roulette Service uses its own PostgreSQL database. In `application.yaml`, the local setup is:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:15433/roulette
```

In Docker, the database is started through `roulette/compose.yaml`. The table for stored games is
derived by JPA/Hibernate from the entity `RouletteGameEntity`.

Currently, `ddl-auto: update` is set. This is practical for development because Hibernate can
automatically create or update the table. For production systems, migration tools such as Flyway or
Liquibase would be cleaner, but for the assignment `update` is understandable and simple.

## Swagger

The Springdoc OpenAPI dependency provides a Swagger UI for the Roulette Service:

```text
http://localhost:8081/swagger-ui/index.html
```

All endpoints can be tested there.

## Current Status

The Roulette Service fulfills the central functionality:

- execute a game round
- validate users through Banking
- book wins/losses through Banking
- store games
- return rules
- return chances, RTP, and House Edge
- return global statistics
- return user statistics
- return game history
- return a single game
- delete a single game
- respond to invalid requests and missing resources with matching errors
- provide focused automated tests for controllers, services, game logic, strategies, validation,
  mapping, entity creation, and the Banking REST client

Still useful before submission:

- manually test the Swagger calls again after the last refactoring
