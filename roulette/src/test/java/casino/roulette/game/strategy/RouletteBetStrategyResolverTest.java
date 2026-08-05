package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouletteBetStrategyResolverTest {

    @Test
    void resolvesStrategyByBetType() {
        RouletteBetStrategyResolver resolver = new RouletteBetStrategyResolver(List.of(
                new StraightNumberBetStrategy(),
                new ColorBetStrategy(),
                new ParityBetStrategy(),
                new RangeBetStrategy(),
                new DozenBetStrategy()
        ));

        assertInstanceOf(StraightNumberBetStrategy.class, resolver.resolve(BetType.STRAIGHT_NUMBER));
        assertInstanceOf(ColorBetStrategy.class, resolver.resolve(BetType.COLOR));
        assertInstanceOf(ParityBetStrategy.class, resolver.resolve(BetType.PARITY));
        assertInstanceOf(RangeBetStrategy.class, resolver.resolve(BetType.RANGE));
        assertInstanceOf(DozenBetStrategy.class, resolver.resolve(BetType.DOZEN));
    }

    @Test
    void rejectsUnregisteredOrMissingBetType() {
        RouletteBetStrategyResolver resolver = new RouletteBetStrategyResolver(List.of());

        assertThrows(BadRouletteRequestException.class, () -> resolver.resolve(BetType.COLOR));
        assertThrows(BadRouletteRequestException.class, () -> resolver.resolve(null));
    }
}
