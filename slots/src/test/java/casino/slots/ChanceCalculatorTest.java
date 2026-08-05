package casino.slots;

import casino.slots.domain.config.SlotPropertiesConfig;
import casino.slots.domain.enums.Symbol;
import casino.slots.service.ChanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChanceCalculatorTest {

    private ChanceCalculator chanceCalculator;
    private SlotPropertiesConfig config;

    @BeforeEach
    void setUp() {
        config = new SlotPropertiesConfig();

        config.setNumberOfFields(3);
        config.setWeights(Map.of(
                Symbol.CHERRY, 8,
                Symbol.LEMON, 2
        ));

        chanceCalculator = new ChanceCalculator(config);
    }

    @Test
    void shouldCalculateChancePerSymbolFromWeights() {
        Map<Symbol, Double> chances =
                chanceCalculator.calculateChancesPerSymbol();

        assertEquals(0.8, chances.get(Symbol.CHERRY), 0.000001);
        assertEquals(0.2, chances.get(Symbol.LEMON), 0.000001);
    }

    @Test
    void shouldReturnChancesThatAddUpToOne() {
        Map<Symbol, Double> chances =
                chanceCalculator.calculateChancesPerSymbol();

        double totalChance = chances.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        assertEquals(1.0, totalChance, 0.000001);
    }

    @Test
    void shouldCalculateChanceForExactlyTwoMatches() {
        double result = chanceCalculator.comboChance(0.5, 2);

        assertEquals(0.375, result, 0.000001);
    }

    @Test
    void shouldCalculateChanceForThreeMatches() {

        double result = chanceCalculator.comboChance(0.5, 3);

        assertEquals(0.125, result, 0.000001);
    }

    @Test
    void shouldReturnZeroWhenSymbolChanceIsZero() {
        double result = chanceCalculator.comboChance(0.0, 3);

        assertEquals(0.0, result, 0.000001);
    }

    @Test
    void shouldReturnValueBetweenZeroAndOne() {
        double result = chanceCalculator.comboChance(0.4, 2);

        assertTrue(result >= 0.0);
        assertTrue(result <= 1.0);
    }
}