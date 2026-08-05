package casino.slots.service;

import casino.slots.domain.enums.Symbol;
import casino.slots.model.SlotsGameEntity;
import casino.slots.model.SlotsGameEntityFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatsCalculatorImplTest {

    private StatsCalculatorImpl calculator;
    private List<SlotsGameEntity> games;

    @BeforeEach
    void setUp() {
        calculator = new StatsCalculatorImpl();

        games = List.of(
                createGame(
                        1L,
                        false,
                        "-10.00",
                        "10.00"
                ),
                createGame(
                        1L,
                        true,
                        "20.00",
                        "10.00"
                ),
                createGame(
                        2L,
                        false,
                        "-5.00",
                        "5.00"
                )
        );
    }

    @Test
    void shouldCountDistinctClients() {
        long result = calculator.totalClientCount(games);

        assertEquals(2L, result);
    }

    @Test
    void shouldCountAllGames() {
        long result = calculator.totalGamesCount(games);

        assertEquals(3L, result);
    }

    @Test
    void shouldCalculateTotalTurnover() {
        BigDecimal result = calculator.totalTurnover(games);

        assertBigDecimalEquals("25.00", result);
    }

    @Test
    void shouldCalculateTotalWinnings() {
        BigDecimal result = calculator.totalWinnings(games);

        assertBigDecimalEquals("20.00", result);
    }

    @Test
    void shouldCalculateTotalLossesAsPositiveNumber() {
        BigDecimal result = calculator.totalLosses(games);

        assertBigDecimalEquals("15.00", result);
    }

    @Test
    void shouldCalculateTotalClientProfit() {
        BigDecimal result = calculator.totalClientProfit(games);

        assertBigDecimalEquals("5.00", result);
    }

    @Test
    void shouldCalculateTotalHouseProfit() {
        BigDecimal result =
                calculator.totalHouseProfitFromClient(games);

        /*
         * Player gets 5 Euro.
         * The house gives Money to the player, that's why -5 Euro.
         */
        assertBigDecimalEquals("-5.00", result);
    }

    @Test
    void shouldReturnZeroValuesForEmptyGameList() {
        List<SlotsGameEntity> emptyGames = List.of();

        assertEquals(
                0L,
                calculator.totalClientCount(emptyGames)
        );

        assertEquals(
                0L,
                calculator.totalGamesCount(emptyGames)
        );

        assertBigDecimalEquals(
                "0",
                calculator.totalTurnover(emptyGames)
        );

        assertBigDecimalEquals(
                "0",
                calculator.totalWinnings(emptyGames)
        );

        assertBigDecimalEquals(
                "0",
                calculator.totalLosses(emptyGames)
        );

        assertBigDecimalEquals(
                "0",
                calculator.totalClientProfit(emptyGames)
        );

        assertBigDecimalEquals(
                "0",
                calculator.totalHouseProfitFromClient(emptyGames)
        );
    }

    private SlotsGameEntity createGame(
            Long userId,
            boolean winning,
            String amount,
            String betAmount
    ) {
        return SlotsGameEntityFactory.create(
                userId,
                winning,
                new BigDecimal(amount),
                new BigDecimal(betAmount),
                List.of(
                        Symbol.CHERRY,
                        Symbol.LEMON,
                        Symbol.ORANGE
                )
        );
    }

    private void assertBigDecimalEquals(
            String expected,
            BigDecimal actual
    ) {
        assertEquals(
                0,
                new BigDecimal(expected).compareTo(actual)
        );
    }
}