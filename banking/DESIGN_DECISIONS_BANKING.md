# Design Decisions – Banking

This one's about the choices that only concern the **Banking** service — and only the ones we actually
made ourselves. Anything the assignment already prescribes (the vertical-slice layout, the User /
Transaction sub-domains, the "call yourself over HTTP" approach for cross-slice logic) isn't repeated
here. Project-wide stuff is in `DESIGN_DECISIONS_GLOBAL.md` in the root.

## Decisions

### 1. The service interfaces are split by what you do with them

Instead of one big `UserService` interface, we cut it into role interfaces: `UserQueryService` for the
reads (`findById`, `findAll`), `UserManagementService` for the writes (`create`, `replace`, `delete`),
and `UserBalanceService` just for the balance (`depositBalanceById`). `UserService` then simply extends
all three. Transactions work the same way — `TransactionQueryService` + `TransactionManagementService`.
That way a caller only ever depends on the slice it actually uses, which is the **Interface Segregation
Principle** (*E-06 Designprinzipien*, lecture), and the reads stay cleanly separated from the writes.

### 2. The known game services are an enum, not a free string

A transaction's invoicing party has to be a known game service, so we modelled that as a `GameService`
enum (`SLOTS`, `ROULETTE`) instead of a plain string. An unknown party can't even get into the system —
anything that isn't a real enum value is rejected with a 400 before we reach any logic. So "is this a
real game service?" is basically answered by the type itself.

### 3. Money conversion lives in one place

The deposit API works with an integer amount plus a decimal count, but everything inside the service is
`BigDecimal`. We keep that back-and-forth in a single `MoneyHelper` instead of scattering the conversion
around, so the "turn amount + decimals into money" logic only exists once (**Don't Repeat Yourself**).

The assignment also hands a money value over as two path parts — an integer `amount` and a `decimals`
value of at most two digits — so we had to decide what `decimals` actually means. We read it as the
fractional cents part and put the two together as `amount + decimals/100`. So `.../deposit/10/50` is
10.50, and `.../deposit/10/5` is 10.05 — a single-digit `decimals` counts as that many cents (0.05), not
tenths (0.50). Anything longer than two digits gets a 400, exactly as the assignment asks.

We ran this past the lecturer in the exercise session and got the go-ahead.
