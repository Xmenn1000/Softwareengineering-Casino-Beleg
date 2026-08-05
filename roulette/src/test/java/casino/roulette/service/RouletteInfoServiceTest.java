package casino.roulette.service;

import casino.roulette.game.strategy.ColorBetStrategy;
import casino.roulette.game.strategy.DozenBetStrategy;
import casino.roulette.game.strategy.ParityBetStrategy;
import casino.roulette.game.strategy.RangeBetStrategy;
import casino.roulette.game.strategy.RouletteBetStrategyResolver;
import casino.roulette.game.strategy.StraightNumberBetStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RouletteInfoServiceTest {

    private final RouletteInfoService infoService = new RouletteInfoService(new RouletteBetStrategyResolver(List.of(
            new StraightNumberBetStrategy(),
            new ColorBetStrategy(),
            new ParityBetStrategy(),
            new RangeBetStrategy(),
            new DozenBetStrategy()
    )));

    @Test
    void getRulesDescribesSupportedEuropeanRouletteBets() {
        String rules = infoService.getRules();

        assertTrue(rules.contains("European roulette"));
        assertTrue(rules.contains("STRAIGHT_NUMBER"));
        assertTrue(rules.contains("COLOR"));
        assertTrue(rules.contains("PARITY"));
        assertTrue(rules.contains("RANGE"));
        assertTrue(rules.contains("DOZEN"));
    }

    @Test
    void getChancesContainsRtpAndHouseEdgeForSupportedBets() {
        String chances = infoService.getChances();

        assertTrue(chances.contains("37 possible ball positions"));
        assertTrue(chances.contains("RTP 36/37"));
        assertTrue(chances.contains("house edge 1/37"));
        assertTrue(chances.contains("STRAIGHT_NUMBER"));
        assertTrue(chances.contains("DOZEN"));
    }
}
