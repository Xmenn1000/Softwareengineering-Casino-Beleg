# Roulette Service README

## Kurzbeschreibung

Der Roulette Service bildet den Spiel-Service fuer europaeisches Roulette im Casino-Projekt ab. Er
stellt REST-Endpunkte bereit, ueber die ein Spieler eine Roulette-Runde spielen, Spielregeln und
Gewinnchancen abrufen sowie globale, benutzerbezogene und spielbezogene Statistiken einsehen kann.

Der Service speichert jede gespielte Runde in einer eigenen PostgreSQL-Datenbank. Fuer
Benutzerpruefung und Kontobewegungen kommuniziert er mit dem Banking Service. Dadurch verwaltet der
Roulette Service selbst keine Benutzerkonten und keine Kontostaende, sondern konzentriert sich auf
seine eigene Domaene: Roulette-Spielregeln, Spielauswertung, Spielhistorie und Roulette-Statistiken.

## Grundlegender Aufbau

Der Roulette Service ist weniger streng nach Vertical Slice Architecture aufgebaut als der Banking
Service. Das passt zur Vorgabe aus dem Beleg: Der Banking Service ist staerker nach Subdomaenen wie
User, Transaction und Stat gesliced, waehrend die Spiel-Services einfacher geschichtet aufgebaut
werden duerfen.

Die wichtigsten Schichten sind:

- `controller`: REST-Schnittstelle nach aussen.
- `service`: Anwendungslogik und Koordination der anderen Komponenten.
- `game`: eigentliche Roulette-Spiellogik.
- `validation`: zentrale Request-Validierung.
- `repository`: Datenbankzugriff.
- `model`: persistierte Datenbank-Entity.
- `view`: DTOs fuer REST-Responses.
- `request`: DTOs fuer REST-Requests.
- `mapper`: Umwandlung von Entity zu DTO.
- `requestClients`: Kommunikation mit anderen Services, hier Banking.
- `config`: externe Konfiguration und Spring Beans.
- `exceptions`: eigene fachliche Fehler mit passenden HTTP-Statuscodes.
- `util`: Roulette-Regeln und Enum-Typen.

Der grobe Ablauf bei einem Spiel ist:

```text
Player
  -> RouletteController
  -> RouletteServiceImpl
  -> RouletteRequestValidator
  -> BankingRestClient: User pruefen
  -> RouletteEngine: Spiel auswerten
  -> BankingRestClient: Gewinn/Verlust buchen
  -> RouletteGameRepository: Spiel speichern
  -> RouletteGameMapper: Entity zu Response-DTO
  -> Player
```

## REST-API

Die REST-API ist in `RouletteApi` definiert und wird durch `RouletteController` umgesetzt.

### `POST /casino/roulette/api/play`

Startet genau eine Roulette-Runde.

Request:

```json
{
  "user": 1,
  "betType": "COLOR",
  "betValue": "RED",
  "amount": 10.00
}
```

Der Service prueft zuerst, ob der Request formal gueltig ist. Danach wird ueber den Banking Service
geprueft, ob der Benutzer existiert. Anschliessend wird die Roulette-Runde ausgewertet, der Gewinn
oder Verlust beim Banking Service gebucht und das Spiel in der Roulette-Datenbank gespeichert.

### `GET /casino/roulette/api/info/rules`

Gibt die unterstuetzten Roulette-Regeln als Text zurueck. Dazu gehoeren die Spielvariante, die
unterstuetzten Wettarten und das Verhalten der Zahl `0`.

### `GET /casino/roulette/api/info/chances`

Gibt Gewinnwahrscheinlichkeiten, Auszahlungen, Return to Player und House Edge aus.

### `GET /casino/roulette/api/stats`

Gibt globale Roulette-Statistiken zurueck, z.B. Anzahl unterschiedlicher Spieler, Anzahl Spiele,
Gesamtumsatz, Gesamtauszahlung und Gewinn des Hauses.

### `GET /casino/roulette/api/stats/user/{userId}`

Gibt Statistiken fuer einen konkreten Benutzer zurueck. Die User-ID wird validiert und der Benutzer
wird ueber den Banking Service geprueft. Existiert der Benutzer nicht, kommt ein `404 Not Found`.
Existiert der Benutzer, hat aber noch keine Roulette-Spiele, koennen die Statistikwerte `0` sein.

### `GET /casino/roulette/api/stats/games`

Gibt eine Liste aller gespeicherten Roulette-Spiele zurueck.

### `GET /casino/roulette/api/stat/{gameId}`

Gibt ein einzelnes gespeichertes Spiel anhand seiner Spiel-ID zurueck. Falls die Spiel-ID nicht
existiert, wird ein `404 Not Found` zurueckgegeben.

### `DELETE /casino/roulette/api/stat/{gameId}`

Loescht ein gespeichertes Spiel anhand seiner Spiel-ID und gibt das geloeschte Spiel noch einmal als
DTO zurueck. Falls die Spiel-ID nicht existiert, wird ein `404 Not Found` zurueckgegeben.

## Wichtige Klassen und Methoden

## Startklasse

### `RouletteApplication`

Diese Klasse startet die Spring-Boot-Anwendung. Sie enthaelt die `main`-Methode und sorgt ueber
`SpringApplication.run(...)` dafuer, dass Spring den Application Context startet, Beans erstellt und
die REST-Endpunkte verfuegbar macht.

## Controller-Schicht

### `RouletteApi`

`RouletteApi` ist ein Interface, das die REST-Endpunkte beschreibt. Dort stehen die Annotationen wie
`@PostMapping`, `@GetMapping`, `@DeleteMapping` und `@RequestMapping`.

Warum als Interface?

Dadurch ist die API-Signatur getrennt von der konkreten Controller-Implementierung. Man sieht an
einer zentralen Stelle, welche Endpunkte der Roulette Service anbietet. Dieses Muster passt auch gut
zum Stil der anderen Services, weil Controller dadurch schlanker bleiben.

Wichtige Methoden:

- `play(...)`: beschreibt den Endpunkt fuer eine neue Spielrunde.
- `getRules()`: beschreibt den Endpunkt fuer Spielregeln.
- `getChances()`: beschreibt den Endpunkt fuer Gewinnchancen.
- `getStats()`: beschreibt den Endpunkt fuer globale Statistiken.
- `getUserStats(...)`: beschreibt den Endpunkt fuer benutzerbezogene Statistiken.
- `getGames()`: beschreibt den Endpunkt fuer alle gespeicherten Spiele.
- `getGame(...)`: beschreibt den Endpunkt fuer ein einzelnes Spiel.
- `deleteGame(...)`: beschreibt den Endpunkt zum Loeschen eines Spiels.

### `RouletteController`

`RouletteController` implementiert `RouletteApi`. Er enthaelt bewusst kaum eigene Logik. Seine
Aufgabe ist es, HTTP-Requests entgegenzunehmen und an den `RouletteService` weiterzugeben.

Beispiel:

```java
return ResponseEntity.ok(rouletteService.play(request));
```

Der Controller entscheidet also nicht selbst, wie Roulette funktioniert. Das ist wichtig, weil
Controller sonst schnell zu gross werden. Die fachliche Logik liegt stattdessen im Service und in
der Game-Schicht.

### `RouletteAdviceController`

`RouletteAdviceController` behandelt zentrale Fehler fuer den Roulette Controller. Er faengt eigene
`HttpException`-Klassen ab und wandelt sie in HTTP-Responses mit `ProblemDetail` um.

Dadurch muss nicht jede Controller-Methode selbst `try/catch` verwenden. Fachliche Fehler koennen an
der Stelle entstehen, an der sie fachlich sinnvoll sind, und werden spaeter zentral in eine
REST-Antwort uebersetzt.

## Service-Schicht

### `RouletteService`

`RouletteService` ist das Interface fuer die Anwendungslogik. Es beschreibt, welche Operationen der
Service anbietet, ohne festzulegen, wie sie intern umgesetzt werden.

Warum ein Interface?

Ein Interface ist hier nicht zwingend notwendig, aber sinnvoll, weil es die Grenze zwischen
Controller und Service klar macht. Ausserdem passt es zum Stil vieler Spring-Anwendungen und
erleichtert spaeter Tests oder alternative Implementierungen.

### `RouletteServiceImpl`

`RouletteServiceImpl` ist die zentrale Koordinationsklasse. Sie verbindet Controller, Validierung,
Banking Client, Game Engine, Repository, Statistikberechnung und Mapper.

Wichtige Methode: `play(RoulettePlayRequestDTO request)`

Ablauf:

1. `rouletteRequestValidator.validatePlayRequest(request)`
Prueft, ob Request, User-ID, Bet-Type und Einsatz gueltig sind.

2. `bankingRestClient.findUserById(request.getUser())`
Prueft beim Banking Service, ob der Benutzer existiert.

3. `rouletteEngine.play(request)`
Fuehrt die eigentliche Roulette-Runde aus.

4. `bankingRestClient.createRouletteTransaction(...)`
Bucht Gewinn oder Verlust beim Banking Service.

5. `rouletteGameRepository.save(game)`
Speichert die gespielte Runde in der Roulette-Datenbank.

6. `RouletteGameMapper.toPlayResultDto(savedGame)`
Wandelt die Entity in ein Response-DTO um.

Wichtige Methode: `getUserStats(Long userId)`

Diese Methode validiert die User-ID und prueft ebenfalls ueber Banking, ob der Benutzer existiert.
Danach werden alle Roulette-Spiele dieses Benutzers aus der Datenbank gelesen und durch
`RouletteStatsCalculator` ausgewertet.

Warum wird Banking hier auch abgefragt?

Der Endpunkt bezieht sich auf einen konkreten Benutzer. Deshalb ist es fachlich sauberer, zwischen
"Benutzer existiert nicht" und "Benutzer existiert, hat aber noch keine Roulette-Spiele" zu
unterscheiden.

### `RouletteInfoService`

Diese Klasse erzeugt die Texte fuer:

- Spielregeln
- Gewinnchancen
- RTP
- House Edge

Die Klasse wurde ausgelagert, damit `RouletteServiceImpl` nicht zu viel Textlogik enthaelt. Der
Service soll koordinieren, aber nicht lange Info-Texte zusammenbauen.

Wichtige Methoden:

- `getRules()`: liefert die textuelle Beschreibung der Roulette-Regeln.
- `getChances()`: liefert Wahrscheinlichkeiten, Auszahlungen, RTP und House Edge.

### `RouletteStatsCalculator`

Diese Klasse berechnet Statistiken aus gespeicherten `RouletteGameEntity`-Objekten.

Wichtige Methoden:

- `calculateStats(List<RouletteGameEntity> games)`
Berechnet globale Statistiken ueber alle Spiele.

- `calculateUserStats(Long userId, List<RouletteGameEntity> games)`
Berechnet Statistiken fuer einen bestimmten Benutzer.

Wichtige Werte:

- `totalClientCount`: Anzahl unterschiedlicher Spieler.
- `totalGamesCount`: Anzahl aller Spiele.
- `totalCashOut`: Summe aller positiven Gewinne, die an Spieler ausgezahlt wurden.
- `totalTurnover`: Summe aller Einsaetze.
- `totalClientProfit`: Nettoergebnis aus Sicht aller Spieler.
- `totalProfit`: Nettoergebnis aus Sicht des Hauses.

Die Klasse verwendet Java Streams, weil die Statistikberechnung dadurch gut als Datenfluss lesbar
ist: Liste nehmen, Werte extrahieren, filtern, summieren.

## Validation-Schicht

### `RouletteRequestValidator`

Diese Klasse buendelt die grundlegende Request-Validierung.

Wichtige Methoden:

- `validatePlayRequest(RoulettePlayRequestDTO request)`
Prueft den gesamten Request fuer `/play`.

- `validateUserId(Long userId)`
Prueft, ob eine User-ID vorhanden und groesser als `0` ist.

Warum wurde diese Klasse ausgelagert?

Vorher lag ein Teil der Validierung im Service und ein Teil in der Engine. Dadurch gab es doppelte
Pruefungen, z.B. fuer leere Requests oder leere User-IDs. Mit dem Validator ist klarer: Grundlegende
Request-Validierung findet an einer zentralen Stelle statt.

Warum liegt nicht jede Validierung dort?

Roulette-spezifische Regeln wie "COLOR darf nur RED oder BLACK sein" liegen weiterhin in
`RouletteRules`, weil sie nicht nur technische Request-Validierung sind, sondern echte Spielregeln.
Entity-Schutz liegt weiterhin in `RouletteGameEntity`, damit keine ungueltige Entity gebaut werden
kann.

## Game-Schicht

### `RouletteEngine`

`RouletteEngine` fuehrt eine einzelne Roulette-Runde aus.

Wichtige Methode: `play(RoulettePlayRequestDTO request)`

Ablauf:

1. `spinGenerator.spin()`
Erzeugt eine zufaellige Ballposition zwischen `0` und `36`.

2. `RouletteRules.isWinningBet(...)`
Prueft, ob die Wette gewonnen hat.

3. `calculateAmount(...)`
Berechnet den Gewinn oder Verlust.

4. `RouletteGameEntity.create(...)`
Erstellt eine persistierbare Entity fuer die gespielte Runde.

Warum wurde die Validierung aus der Engine entfernt?

Die Engine soll moeglichst nur Spielauswertung machen. Request-Validierung gehoert in den Validator,
Banking-Pruefung in den Service und die Entity schuetzt ihre eigenen Daten. Dadurch hat jede Klasse
eine klarere Aufgabe.

### `RouletteSpinGenerator`

`RouletteSpinGenerator` ist ein Interface fuer das Drehen des Roulette-Rads.

Wichtige Methode:

- `spin()`: gibt eine Zahl zwischen `0` und `36` zurueck.

Warum ein Interface?

Damit die Engine nicht direkt von Zufallslogik abhaengt. Fuer Tests kann spaeter ein fake oder
fester Spin Generator verwendet werden, z.B. einer, der immer `17` zurueckgibt. Das macht die
Spielauswertung testbar.

### `RandomRouletteSpinGenerator`

Diese Klasse implementiert `RouletteSpinGenerator` und erzeugt eine echte zufaellige Ballposition.

Die Methode:

```java
ThreadLocalRandom.current().nextInt(37)
```

liefert Werte von `0` bis `36`. Die obere Grenze `37` ist exklusiv, deshalb ist `36` der groesste
moegliche Wert.

### `RouletteRules`

`RouletteRules` enthaelt die eigentlichen Roulette-Regeln.

Wichtige Methoden:

- `isWinningBet(BetType betType, String betValue, int ballPosition)`
Verteilt die Pruefung auf die passende Wettart.

- `payoutMultiplier(BetType betType)`
Gibt den Gewinnmultiplikator zurueck.

- `hitProbability(BetType betType)`
Berechnet die Trefferwahrscheinlichkeit.

- `returnToPlayer(BetType betType)`
Berechnet den theoretischen Return to Player.

- `houseEdge(BetType betType)`
Berechnet den Hausvorteil.

Unterstuetzte Wettarten:

- `STRAIGHT_NUMBER`: eine einzelne Zahl von `0` bis `36`, Auszahlung `35:1`.
- `COLOR`: `RED` oder `BLACK`, Auszahlung `1:1`.
- `PARITY`: `EVEN` oder `ODD`, Auszahlung `1:1`.
- `RANGE`: `LOW` oder `HIGH`, Auszahlung `1:1`.
- `DOZEN`: `FIRST`, `SECOND` oder `THIRD`, Auszahlung `2:1`.

Warum europaeisches Roulette?

Europaeisches Roulette hat die Zahlen `0` bis `36`, also 37 Felder. Amerikanisches Roulette haette
zusaetzlich `00` und dadurch 38 Felder. Die europaeische Variante ist einfacher abzubilden, im
deutschen/europaeischen Kontext naheliegender und hat einen klaren House Edge von `1/37`.

Warum ist die `0` wichtig?

Die Zahl `0` sorgt dafuer, dass einfache Wetten wie Rot/Schwarz, Gerade/Ungerade oder Niedrig/Hoch
nicht exakt fair sind. Bei Rot/Schwarz gibt es 18 Gewinnfelder und 18 Verlustfelder plus die `0`.
Dadurch gewinnt das Haus langfristig.

Warum wird House Edge nicht extra vom Gewinn abgezogen?

Der House Edge entsteht automatisch durch die Kombination aus:

- 37 moeglichen Feldern
- normalen Auszahlungen
- Zahl `0`

Beispiel Rot/Schwarz:

- Gewinnchance: `18/37`
- Auszahlung bei Gewinn: `1:1`
- Verlustchance: `19/37`

Langfristig ergibt sich daraus ein Hausvorteil von `1/37`. Wenn man den House Edge zusaetzlich
abziehen wuerde, waere das mathematisch doppelt und damit falsch.

Warum ist House Edge nicht in der `application.yaml`?

Der House Edge ist keine frei einstellbare technische Konfiguration, sondern eine mathematische
Folge der Roulette-Regeln. Wenn man ihn in die Config legen wuerde, koennte dort ein Wert stehen,
der gar nicht zu den echten Regeln passt.

Konfigurierbar sind deshalb nur Werte, die wirklich betriebliche Einstellungen sind, z.B.:

- minimaler Einsatz
- maximaler Einsatz
- Banking-Service-URL
- Name des Services fuer Banking-Transaktionen

Die Spielmathematik bleibt im Code bei `RouletteRules`.

## Persistence-Schicht

### `RouletteGameEntity`

Diese Entity repraesentiert eine gespeicherte Roulette-Runde in der Datenbanktabelle
`roulette_games`.

Wichtige Felder:

- `id`: technische Spiel-ID.
- `user`: ID des Spielers.
- `winning`: ob die Runde gewonnen wurde.
- `amount`: Netto-Gewinn oder Netto-Verlust aus Spielersicht.
- `betAmount`: urspruenglicher Einsatz.
- `betType`: Art der Wette.
- `betValue`: konkreter Wert der Wette.
- `ballPosition`: gezogene Roulette-Zahl.

Wichtige Methode:

- `create(...)`: Factory-Methode zum Erzeugen einer gueltigen Entity.

Warum eine Factory-Methode?

Die Entity hat einen geschuetzten No-Args-Konstruktor fuer JPA. Fachlich soll eine Entity aber nicht
halbgueltig gebaut werden. Deshalb werden neue Entities ueber `create(...)` erzeugt. Dort werden
zentrale Entity-Regeln geprueft, bevor das Objekt entsteht.

Warum wird `betValue` normalisiert?

`betValue` wird mit `trim().toUpperCase()` gespeichert. Dadurch werden Eingaben wie `" red "`,
`"Red"` und `"RED"` einheitlich als `"RED"` gespeichert. Das macht Datenbankeintraege konsistenter.

### `RouletteGameRepository`

Das Repository erweitert `JpaRepository<RouletteGameEntity, Long>`.

Dadurch stellt Spring Data JPA automatisch Methoden bereit, z.B.:

- `findAll()`
- `findById(...)`
- `save(...)`
- `delete(...)`

Zusaetzlich gibt es:

- `findByUser(Long user)`

Diese Methode wird von Spring Data JPA aus dem Methodennamen abgeleitet. Spring erkennt: Suche alle
`RouletteGameEntity`-Eintraege, deren Feld `user` dem uebergebenen Wert entspricht.

## Mapper und DTOs

### `RouletteGameMapper`

Der Mapper wandelt Datenbank-Entities in DTOs fuer die REST-Ausgabe um.

Wichtige Methoden:

- `toGameDto(RouletteGameEntity entity)`
- `toPlayResultDto(RouletteGameEntity entity)`

Warum ein Mapper?

Die Entity gehoert zur Datenbank-Schicht. Die DTOs gehoeren zur REST-Schicht. Durch den Mapper
werden diese Schichten getrennt. So muss nicht die Datenbankstruktur direkt als API-Struktur nach
aussen gegeben werden.

### Request-DTO

### `RoulettePlayRequestDTO`

Dieses DTO beschreibt den JSON-Body fuer `/play`.

Felder:

- `user`
- `betType`
- `betValue`
- `amount`

Es wird bewusst camelCase verwendet, weil das laut Ruecksprache mit dem Professor erlaubt ist.
Deshalb werden keine `@JsonProperty`-Annotationen gebraucht.

### Response-DTOs im Package `view`

Die DTOs im Package `view` bilden die JSON-Ausgaben der REST-API ab. Im Beleg ist mit "View" nicht
ein HTML-Dokument gemeint, sondern die aeussere Darstellung der REST-Daten.

Wichtige DTOs:

- `RoulettePlayResultDTO`: Ergebnis einer gespielten Runde.
- `RouletteGameDTO`: gespeichertes Spiel.
- `RouletteStatsDTO`: globale Statistiken.
- `RouletteUserStatsDTO`: benutzerbezogene Statistiken.

## Banking-Kommunikation

### `BankingRestClient`

Diese Klasse kommuniziert mit dem Banking Service ueber HTTP.

Wichtige Methoden:

- `findUserById(Long userId)`
Fragt den Banking Service, ob ein Benutzer existiert.

- `createRouletteTransaction(Long userId, BigDecimal amount)`
Erstellt beim Banking Service eine Transaktion fuer Gewinn oder Verlust.

Warum gibt es diese Klasse?

Der Roulette Service soll nicht wissen muessen, wie HTTP-Aufrufe technisch gebaut werden. Diese
Verantwortung liegt im Client. Dadurch bleibt `RouletteServiceImpl` besser lesbar.

### `BankingUserDTO`

DTO fuer die Antwort des Banking Service beim Laden eines Benutzers.

### `BankingTransactionRequestDTO`

DTO fuer den Request an den Banking Service, wenn eine Roulette-Transaktion erstellt wird.

Der `amount` ist dabei das Nettoergebnis aus Sicht des Spielers:

- positiver Betrag: Spieler gewinnt Geld.
- negativer Betrag: Spieler verliert Geld.

## Konfiguration

### `RouletteConfig`

Diese Klasse erstellt den `RestClient` fuer den Banking Service.

Wichtige Methode:

- `bankRestClient(...)`

Die Base-URL kommt aus der `application.yaml`.

### `RouletteProperties`

Diese Klasse bindet Werte aus der `application.yaml` an ein Java-Objekt.

Aktuell konfigurierbar:

- `casino.roulette.betting.minAmount`
- `casino.roulette.betting.maxAmount`
- `casino.roulette.banking.invoicingParty`

Warum diese Werte in der Config?

Diese Werte sind betriebliche Einstellungen. Es ist realistisch, dass man sie spaeter ohne
Codeaenderung anpassen moechte.

Warum nicht alle Spielregeln in der Config?

Spielregeln wie Auszahlungen, Gewinnwahrscheinlichkeiten oder House Edge gehoeren fachlich zusammen.
Wenn man einzelne Werte davon frei konfigurierbar macht, koennen schnell widerspruechliche Regeln
entstehen. Deshalb liegen diese Regeln gebuendelt in `RouletteRules`.

## Exceptions

### `HttpException`

Basisklasse fuer fachliche HTTP-Fehler. Sie enthaelt einen HTTP-Status und eine Fehlermeldung.

### `BadRouletteRequestException`

Wird verwendet, wenn ein Request fachlich oder formal ungueltig ist. Ergebnis: `400 Bad Request`.

Beispiele:

- leerer Request
- User-ID kleiner oder gleich `0`
- fehlender Einsatz
- Einsatz kleiner als Mindesteinsatz
- ungueltiger Wettwert

### `BankingUserNotFoundException`

Wird verwendet, wenn der Banking Service einen Benutzer nicht findet. Ergebnis: `404 Not Found`.

### `RouletteGameNotFoundException`

Wird verwendet, wenn ein gespeichertes Roulette-Spiel nicht gefunden wird. Ergebnis: `404 Not
Found`.

## Wichtige Architekturentscheidungen

### Warum geschichtete Architektur statt Vertical Slice?

Der Banking Service ist komplexer, weil er mehrere Subdomaenen wie User, Transaction und Stat hat.
Dort ist Vertical Slice Architecture sinnvoll, weil jede Subdomaene eigene Controller, Handler und
Datenzugriffe haben kann.

Der Roulette Service ist kleiner und fachlich enger. Die Hauptdomaene ist das Spiel Roulette.
Deshalb ist eine geschichtete Architektur uebersichtlicher:

- Controller fuer REST
- Service fuer Koordination
- Engine fuer Spiellogik
- Repository fuer Datenbank
- Client fuer Banking

Das entspricht auch der Belegvorgabe, dass die Spiel-Services weniger streng geschichtet aufgebaut
werden duerfen.

### Warum DTOs statt Entities direkt ausgeben?

Entities gehoeren zur Datenbank. DTOs gehoeren zur API. Wenn man Entities direkt ausgibt, koppelt
man die API stark an die Datenbankstruktur. Mit DTOs kann die API stabil bleiben, auch wenn sich
intern die Entity aendert.

### Warum `BigDecimal` fuer Geld?

Geld sollte nicht mit `double` oder `float` berechnet werden, weil diese Typen Rundungsfehler
erzeugen koennen. `BigDecimal` ist fuer Geldbetraege besser geeignet und wird auch in den
Beleganforderungen gefordert.

### Warum `Long` fuer IDs?

Die Beleganforderungen sehen Long-IDs vor. Ausserdem ist `Long` bei Datenbank-IDs in Spring/JPA sehr
ueblich und bietet genug Wertebereich.

### Warum wird der Gewinn als Nettoergebnis gespeichert?

Das Feld `amount` beschreibt das Ergebnis aus Sicht des Spielers:

- Verlust: `-betAmount`
- Gewinn: `betAmount * payoutMultiplier`

Der urspruengliche Einsatz bleibt separat in `betAmount` gespeichert. Dadurch koennen Statistiken
sowohl Gewinn/Verlust als auch Umsatz sauber berechnen.

### Warum wird die Banking-Transaktion nach der Spielauswertung erstellt?

Erst nach der Spielauswertung steht fest, ob der Spieler gewonnen oder verloren hat. Danach kann der
Roulette Service genau diesen Betrag an Banking senden.

Aktueller Ablauf:

1. User existiert?
2. Spiel auswerten.
3. Gewinn/Verlust buchen.
4. Spiel speichern.

### Warum wird der User nicht in der Roulette-Datenbank gespeichert?

Der Roulette Service speichert nur die User-ID. Die eigentlichen Benutzerdaten gehoeren dem Banking
Service. Das trennt die Verantwortlichkeiten der Services:

- Banking verwaltet Benutzer und Konten.
- Roulette verwaltet Roulette-Spiele und Roulette-Statistiken.

### Warum ist die Zahlengenerierung ausgelagert?

Durch `RouletteSpinGenerator` kann die Zufallszahl spaeter in Tests ersetzt werden. Ohne diese
Auslagerung waere die Engine schwer testbar, weil jedes Spiel zufaellig waere.

### Warum keine zusaetzlichen Wettarten?

Roulette bietet noch viele weitere Wettarten, z.B. Split, Street, Corner, Six Line oder Column. Fuer
den Beleg wurden die wichtigsten und gut nachvollziehbaren Wettarten umgesetzt:

- Straight Number
- Color
- Parity
- Range
- Dozen

Damit sind die Grundprinzipien von Roulette abgebildet, ohne die Implementierung unnoetig gross zu
machen.

### Warum keine `@JsonProperty`-Annotationen?

Nach Ruecksprache mit dem Professor darf camelCase verwendet werden. Deshalb muessen JSON-Felder wie
`betType`, `betValue` oder `ballPosition` nicht auf snake_case gemappt werden.

### Warum `ProblemDetail` fuer Fehler?

`ProblemDetail` ist eine standardisierte Art, Fehler in REST-APIs zurueckzugeben. Dadurch sehen
Fehlerantworten einheitlicher aus und enthalten neben der Meldung auch den HTTP-Status.

## Datenbank

Der Roulette Service verwendet eine eigene PostgreSQL-Datenbank. In der `application.yaml` steht
lokal:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:15433/roulette
```

In Docker wird die Datenbank ueber `roulette/compose.yaml` gestartet. Die Tabelle fuer gespeicherte
Spiele wird durch JPA/Hibernate aus der Entity `RouletteGameEntity` abgeleitet.

Aktuell ist `ddl-auto: update` gesetzt. Das ist fuer Entwicklung praktisch, weil Hibernate die
Tabelle automatisch anlegen oder aktualisieren kann. Fuer produktive Systeme waeren Migrationstools
wie Flyway oder Liquibase sauberer, fuer den Beleg ist `update` aber nachvollziehbar und einfach.

## Swagger

Durch die Springdoc-OpenAPI-Abhaengigkeit stellt der Roulette Service eine Swagger UI bereit:

```text
http://localhost:8081/swagger-ui/index.html
```

Dort koennen alle Endpunkte ausprobiert werden.

## Aktueller Stand

Der Roulette Service erfuellt die zentrale Funktionalitaet:

- Spielrunde ausfuehren
- User ueber Banking pruefen
- Gewinn/Verlust ueber Banking buchen
- Spiel speichern
- Regeln ausgeben
- Chancen, RTP und House Edge ausgeben
- globale Statistiken ausgeben
- Benutzerstatistiken ausgeben
- Spielhistorie ausgeben
- einzelnes Spiel anzeigen
- einzelnes Spiel loeschen
- ungueltige Requests und nicht gefundene Ressourcen mit passenden Fehlern beantworten

Noch sinnvoll fuer die Abgabe:

- gezielte Tests fuer Engine, Rules, Validator, Service und Controller
- PlantUML-Diagramm fuer den Roulette Service pflegen
- eventuell lange Lern-Kommentare im Code vor finaler Abgabe kuerzen
