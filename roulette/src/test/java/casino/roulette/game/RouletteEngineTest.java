package casino.roulette.game;

import casino.roulette.game.strategy.ColorBetStrategy;
import casino.roulette.game.strategy.DozenBetStrategy;
import casino.roulette.game.strategy.ParityBetStrategy;
import casino.roulette.game.strategy.RangeBetStrategy;
import casino.roulette.game.strategy.RouletteBetStrategyResolver;
import casino.roulette.game.strategy.StraightNumberBetStrategy;
import casino.roulette.model.RouletteGameEntity;
import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.util.BetType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouletteEngineTest {

    @Test
    void playCreatesWinningGameWithCalculatedAmount() {
        RouletteEngine engine = new RouletteEngine(
                () -> 7,
                resolver()
        );
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                BetType.COLOR,
                "RED",
                new BigDecimal("10.00")
        );

        RouletteGameEntity result = engine.play(request);

        assertTrue(result.isWinning());
        assertEquals(0, new BigDecimal("10.00").compareTo(result.getAmount()));
        assertEquals(0, new BigDecimal("10.00").compareTo(result.getBetAmount()));
        assertEquals(BetType.COLOR, result.getBetType());
        assertEquals("RED", result.getBetValue());
        assertEquals(7, result.getBallPosition());
    }

    @Test
    void playCreatesLosingGameWithNegativeAmount() {
        RouletteEngine engine = new RouletteEngine(
                () -> 7,
                resolver()
        );
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                BetType.COLOR,
                "BLACK",
                new BigDecimal("10.00")
        );

        RouletteGameEntity result = engine.play(request);

        assertFalse(result.isWinning());
        assertEquals(0, new BigDecimal("-10.00").compareTo(result.getAmount()));
    }

    @Test
    void straightNumberWinUsesThirtyFiveToOnePayout() {
        RouletteEngine engine = new RouletteEngine(
                () -> 17,
                resolver()
        );
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                BetType.STRAIGHT_NUMBER,
                "17",
                new BigDecimal("2.00")
        );

        RouletteGameEntity result = engine.play(request);

        assertTrue(result.isWinning());
        assertEquals(0, new BigDecimal("70.00").compareTo(result.getAmount()));
    }

    private RouletteBetStrategyResolver resolver() {
        return new RouletteBetStrategyResolver(List.of(
                new StraightNumberBetStrategy(),
                new ColorBetStrategy(),
                new ParityBetStrategy(),
                new RangeBetStrategy(),
                new DozenBetStrategy()
        ));
    }
}
