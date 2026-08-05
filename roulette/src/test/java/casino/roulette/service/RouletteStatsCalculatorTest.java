package casino.roulette.service;

import casino.roulette.model.RouletteGameEntity;
import casino.roulette.util.BetType;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouletteStatsCalculatorTest {

    private final RouletteStatsCalculator calculator = new RouletteStatsCalculator();

    @Test
    void calculatesGlobalStats() {
        List<RouletteGameEntity> games = List.of(
                game(1L, true, "20.00", "10.00"),
                game(1L, false, "-5.00", "5.00"),
                game(2L, false, "-10.00", "10.00")
        );

        RouletteStatsDTO result = calculator.calculateStats(games);

        assertEquals(2, result.getTotalClientCount());
        assertEquals(3, result.getTotalGamesCount());
        assertBigDecimalEquals("-5.00", result.getTotalProfit());
        assertBigDecimalEquals("20.00", result.getTotalCashOut());
        assertBigDecimalEquals("25.00", result.getTotalTurnover());
    }

    @Test
    void calculatesUserStats() {
        List<RouletteGameEntity> games = List.of(
                game(1L, true, "20.00", "10.00"),
                game(1L, false, "-5.00", "5.00")
        );

        RouletteUserStatsDTO result = calculator.calculateUserStats(1L, games);

        assertEquals(1L, result.getClient());
        assertEquals(2, result.getTotalGamesCount());
        assertBigDecimalEquals("20.00", result.getTotalWinnings());
        assertBigDecimalEquals("5.00", result.getTotalLosses());
        assertBigDecimalEquals("15.00", result.getTotalClientProfit());
        assertBigDecimalEquals("15.00", result.getTotalHouseTurnoverFromClient());
        assertBigDecimalEquals("-15.00", result.getTotalHouseProfitFromClient());
    }

    private RouletteGameEntity game(Long user, boolean winning, String amount, String betAmount) {
        return RouletteGameEntity.create(
                user,
                winning,
                new BigDecimal(amount),
                new BigDecimal(betAmount),
                BetType.COLOR,
                "RED",
                7
        );
    }

    private void assertBigDecimalEquals(String expected, BigDecimal actual) {
        assertEquals(0, new BigDecimal(expected).compareTo(actual));
    }
}
