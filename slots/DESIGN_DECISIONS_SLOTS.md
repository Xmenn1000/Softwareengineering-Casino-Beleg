# Design Decisions – Slots

This one's about the choices that only touch the **Slots** service. Project-wide stuff is in
`DESIGN_DECISIONS_GLOBAL.md` in the root.

## Decisions

### 1. The machine is built from config

We don't hard-code the machine. `SlotPropertiesConfig` pulls the symbol weights, the payout multipliers
and the reel count out of `application.yaml`, and `SlotConfig` wires the pieces together — the
`CashOutMultiplier` (our payout table), the `RuleEngine` and the `SlotMachine`. So the whole machine is
basically assembled from config at startup. It's a tiny bit of **Separation of Concerns**, but honestly
that's not really the point — it's just comfy having all the numbers in the YAML, where config belongs
anyway.

### 2. Payouts are a Strategy pattern behind a `Rule` interface

This is where the SOLID stuff really clicks for us. Instead of one giant `if`/`switch` for what a spin
is worth, every winning pattern — one, two or three of a kind — is its own `ExactCountRule` that does
exactly one thing (**Single Responsibility Principle**). They all share a `Rule` interface, so they're completely swappable and the
engine treats them all the same (**Liskov Substitution Principle**). On top there's a `RuleEngine` that implements the same
interface, holds all the rules (a little Composite), runs them and keeps the biggest payout — that's how
"only the best combo counts" just happens. The engine only knows the `Rule` abstraction, never a
concrete rule (**Dependency Inversion Principle**), and a new payout is just a new rule with nothing else touched (**Open-Closed Principle**).
Strategy + Composite pulling four SOLID principles at once (*E-06 Designprinzipien*, lecture). This was
also a tip you gave us in the exercise session.

### 3. The chances are calculated, not written down

No fixed odds table anywhere. `ChanceCalculator` turns the machine's setup into probabilities — each
symbol's weight into a chance, plus the binomial distribution for the two- and three-of-a-kind odds —
and `InfoService` builds the `/info/chances` text from that. Best part: the weights are the single
source of truth for both the real payouts and the odds we show, so the two can't quietly drift apart
(**Don't Repeat Yourself**, DRY). Hard-coding them would be pointless anyway when you can just change a probability in the YAML.

### 4. The engine hides behind an interface

`SlotEngine` is a tiny one-method interface (`play`), and `SlotMachine` is what's behind it — small on
purpose, so nothing depends on more than it needs (**Interface Segregation Principle**). `PlayService` only ever talks to
`SlotEngine`, never straight to `SlotMachine` (**Dependency Inversion Principle**), which is also what lets a test drop in a fake
engine. Under that, a reel is a `SlotField` (a weighted pool of symbols plus an injected `Random`), and
the machine is just a handful of those together. It also leaves the door open if there's ever another
slot engine down the line.

### 5. A play is one self-contained round

A spin is one `POST /play` straight through `PlayService`: check the request, check the user with the
bank, spin, build the round via the factory + validator, book it as a transaction, save it, and put
together the `SlotsGameResultDTO` at the end. The service just coordinates — every step goes to a
focused helper (`SlotsRequestValidation`, the `BankingRestClient`, the `SlotEngine`, the factory +
validator, the repository), each with its own job (**Separation of Concerns** / **Single Responsibility Principle**). No session, no state between spins —
every round stands on its own, which is exactly the stateless idea REST is built on.

### 6. Game logic stays out of the Spring layer

The actual game lives in its own packages — `domain.machine` for the machine and reels, `domain.ruleSet`
for the rules — and knows nothing about Spring or the web. Controllers, services, repository and DTOs sit
around it. Keeping the domain framework-free is **Separation of Concerns** again, and it's what lets us
build and test the pure game logic on its own (and it'd make life easier if we ever dropped Spring).

### 7. Split into Game, Info and Stats

The assignment wants the game services cut into three sub-domains, so we did exactly that. **Game** is
the play (`PlayController` / `PlayService`), **Info** does the rules and chances (`InfoController` /
`InfoService`), and **Stats** covers the statistics and history (`StatsController` +
`GameHistoryController`). Each one has its own controller, service and DTOs, so nobody ends up with one
giant slots controller doing everything (**Separation of Concerns**).

### 8. Playing is risk-free — no balance gate

A spin always goes through. `PlayService` makes sure the user exists, but it never checks whether they
can "afford" the bet, so a loss can take the balance negative. That's the whole point — the project is
about showing the odds risk-free, not running a real cash gate. The bank just records the money moving,
and we didn't bolt on a balance check the assignment never asked for.
