package casino.slots;

import casino.slots.domain.config.SlotPropertiesConfig;
import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;
import casino.slots.service.ChanceCalculator;
import casino.slots.service.InfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InfoServiceImplTest {

    private InfoServiceImpl infoService;

    @BeforeEach
    void setUp() {
        SlotPropertiesConfig config = new SlotPropertiesConfig();

        config.setNumberOfFields(3);

        config.setWeights(Map.of(
                Symbol.CHERRY, 8,
                Symbol.LEMON, 2,
                Symbol.ORANGE, 1
        ));

        config.setPayout(Map.of(
                ResultPattern.THREE_OF_A_KIND,
                Map.of(
                        Symbol.CHERRY, 8,
                        Symbol.ORANGE, 0
                ),

                ResultPattern.TWO_OF_A_KIND,
                Map.of(
                        Symbol.LEMON, 3
                )
        ));

        ChanceCalculator chanceCalculator =
                new ChanceCalculator(config);

        infoService = new InfoServiceImpl(
                config,
                chanceCalculator
        );
    }

    @Test
    void shouldReturnGameRules() {
        String rules = infoService.getRules();

        assertTrue(rules.contains("Slots - Game Rules"));
        assertTrue(rules.contains("Pull the lever"));
        assertTrue(rules.contains("Three of a kind"));
        assertTrue(rules.contains("/chances"));
        assertTrue(rules.contains("loss"));
    }

    @Test
    void shouldIncludeConfiguredNumberOfReels() {
        String chances = infoService.getChances();

        assertTrue(chances.contains("Each spin turns 3 reels"));
    }

    @Test
    void shouldIncludeConfiguredSymbols() {
        String chances = infoService.getChances();

        assertTrue(chances.contains("CHERRY"));
        assertTrue(chances.contains("LEMON"));
        assertTrue(chances.contains("ORANGE"));
    }

    @Test
    void shouldIncludeConfiguredWinningCombinations() {
        String chances = infoService.getChances();

        assertTrue(chances.contains("3x CHERRY"));
        assertTrue(chances.contains("x8"));

        assertTrue(chances.contains("2x LEMON"));
        assertTrue(chances.contains("x3"));
    }

    @Test
    void shouldNotIncludeCombinationWithZeroMultiplier() {
        String chances = infoService.getChances();

        assertFalse(chances.contains("3x ORANGE"));
    }

    @Test
    void shouldExplainProfitCalculation() {
        String chances = infoService.getChances();

        assertTrue(chances.contains("How your profit is calculated"));
        assertTrue(chances.contains("win  = bet * multiplier"));
        assertTrue(chances.contains("loss = -bet"));
    }
}