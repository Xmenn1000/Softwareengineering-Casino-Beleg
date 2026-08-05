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

### 4. Editing or deleting a transaction is a ledger correction, not a re-booking

`POST` a transaction applies it to the user's balance (via the deposit endpoint). `PUT` and `DELETE` on
a transaction, though, only touch the ledger row — they deliberately **don't** reverse the old amount or
apply the new one to the balance. So after a `PUT`/`DELETE` the ledger and the balances can diverge on
purpose.

We treat these two endpoints as admin-side corrections of the *record*, not as money movements. Doing it
the other way — re-booking the balance on every edit — would mean an edit silently moves real money, and
a delete would have to refund, which turns a "fix a typo in the log" action into a financial transaction
with its own failure modes. The assignment specs `PUT`/`DELETE` purely in terms of the returned
transaction object and never mentions a balance effect, so we kept the balance side out of them
(**Single Responsibility** / **Principle of Least Surprise**: an edit edits the record, a booking books
money — not both at once). If real re-booking were wanted, it'd be a separate, explicit reversal flow.

### 5. Creating a transaction isn't atomic across the self-call — and that's an accepted limit

`createForUserId` does two things in sequence: it calls the deposit endpoint (an HTTP call the service
makes against itself) and then saves the transaction row. Those two steps aren't wrapped in one
transaction — the deposit already committed in its own request by the time we `save`. So if the `save`
fails after a successful deposit, the balance has moved but no ledger entry exists.

This falls straight out of the assignment's "solve cross-slice logic with a call against yourself"
approach: an HTTP boundary can't share a database transaction, so no local `@Transactional` can span it.
A real system would need a compensating action (a reversing deposit) or an outbox/saga to close the gap.
For this scope we accept the window as a known limitation rather than build distributed-transaction
machinery the assignment doesn't ask for (**KISS / YAGNI**). We'd rather have it written down than
pretend the self-call is atomic.
