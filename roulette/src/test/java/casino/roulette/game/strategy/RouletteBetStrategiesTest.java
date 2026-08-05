package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouletteBetStrategiesTest {

    @Test
    void straightNumberWinsOnlyOnMatchingNumber() {
        StraightNumberBetStrategy strategy = new StraightNumberBetStrategy();

        assertTrue(strategy.isWinning("17", 17));
        assertFalse(strategy.isWinning("17", 18));
        assertEquals(BetType.STRAIGHT_NUMBER, strategy.betType());
        assertEquals(35, strategy.payoutMultiplier());
        assertEquals(1, strategy.winningOutcomes());
    }

    @Test
    void straightNumberRejectsInvalidBetValue() {
        StraightNumberBetStrategy strategy = new StraightNumberBetStrategy();

        assertThrows(BadRouletteRequestException.class, () -> strategy.isWinning("37", 17));
        assertThrows(BadRouletteRequestException.class, () -> strategy.isWinning("ABC", 17));
    }

    @Test
    void colorStrategyHandlesRedBlackAndZero() {
        ColorBetStrategy strategy = new ColorBetStrategy();

        assertTrue(strategy.isWinning("RED", 7));
        assertTrue(strategy.isWinning("BLACK", 8));
        assertFalse(strategy.isWinning("RED", 8));
        assertFalse(strategy.isWinning("BLACK", 0));
        assertEquals(BetType.COLOR, strategy.betType());
        assertEquals(1, strategy.payoutMultiplier());
        assertEquals(18, strategy.winningOutcomes());
    }

    @Test
    void colorStrategyRejectsUnsupportedColor() {
        ColorBetStrategy strategy = new ColorBetStrategy();

        assertThrows(BadRouletteRequestException.class, () -> strategy.isWinning("GREEN", 7));
    }

    @Test
    void parityStrategyHandlesEvenOddAndZero() {
        ParityBetStrategy strategy = new ParityBetStrategy();

        assertTrue(strategy.isWinning("EVEN", 18));
        assertTrue(strategy.isWinning("ODD", 17));
        assertFalse(strategy.isWinning("ODD", 18));
        assertFalse(strategy.isWinning("EVEN", 0));
        assertEquals(BetType.PARITY, strategy.betType());
        assertEquals(1, strategy.payoutMultiplier());
        assertEquals(18, strategy.winningOutcomes());
    }

    @Test
    void rangeStrategyHandlesLowHighAndZero() {
        RangeBetStrategy strategy = new RangeBetStrategy();

        assertTrue(strategy.isWinning("LOW", 1));
        assertTrue(strategy.isWinning("LOW", 18));
        assertTrue(strategy.isWinning("HIGH", 19));
        assertTrue(strategy.isWinning("HIGH", 36));
        assertFalse(strategy.isWinning("LOW", 19));
        assertFalse(strategy.isWinning("HIGH", 0));
        assertEquals(BetType.RANGE, strategy.betType());
        assertEquals(1, strategy.payoutMultiplier());
        assertEquals(18, strategy.winningOutcomes());
    }

    @Test
    void dozenStrategyHandlesAllDozensAndZero() {
        DozenBetStrategy strategy = new DozenBetStrategy();

        assertTrue(strategy.isWinning("FIRST", 12));
        assertTrue(strategy.isWinning("SECOND", 13));
        assertTrue(strategy.isWinning("THIRD", 36));
        assertFalse(strategy.isWinning("FIRST", 13));
        assertFalse(strategy.isWinning("THIRD", 0));
        assertEquals(BetType.DOZEN, strategy.betType());
        assertEquals(2, strategy.payoutMultiplier());
        assertEquals(12, strategy.winningOutcomes());
    }

    @Test
    void allStrategiesUseEuropeanRouletteRtpAndHouseEdge() {
        RouletteBetStrategy strategy = new ColorBetStrategy();
        BigDecimal expectedRtp = BigDecimal.valueOf(36)
                .divide(BigDecimal.valueOf(37), MathContext.DECIMAL64);
        BigDecimal expectedHouseEdge = BigDecimal.ONE.subtract(expectedRtp);

        assertEquals(37, strategy.totalOutcomes());
        assertEquals(0, expectedRtp.compareTo(strategy.returnToPlayer()));
        assertEquals(0, expectedHouseEdge.compareTo(strategy.houseEdge()));
    }
}
