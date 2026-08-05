package casino.slots.domain.config;

import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;
import casino.slots.domain.machine.CashOutMultiplier;
import casino.slots.domain.machine.SlotEngine;
import casino.slots.domain.ruleSet.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({
        SlotPropertiesConfig.class,
        SlotConfig.class
})
@TestPropertySource(properties = {
        "slots.machine.numberOfFields=3",
        "slots.machine.rules=Test",

        "slots.machine.weights.CHERRY=8",
        "slots.machine.weights.LEMON=5",
        "slots.machine.weights.SEVEN=1",

        "slots.machine.payout.THREE_OF_A_KIND.CHERRY=8",
        "slots.machine.payout.THREE_OF_A_KIND.SEVEN=1500",
        "slots.machine.payout.TWO_OF_A_KIND.SEVEN=10",
        "slots.machine.payout.ONE_OF_A_KIND.SEVEN=2"
})
class SlotConfigurationTest {

    @Autowired
    private SlotPropertiesConfig properties;

    @Autowired
    private CashOutMultiplier cashOutMultiplier;

    @Autowired
    private Rule rule;

    @Autowired
    private SlotEngine slotEngine;

    @Test
    void shouldBindGeneralMachineProperties() {
        assertEquals(3, properties.getNumberOfFields());
        assertEquals("Test", properties.getRules());
    }

    @Test
    void shouldBindSymbolWeights() {
        assertNotNull(properties.getWeights());

        assertEquals(8, properties.getWeights().get(Symbol.CHERRY));
        assertEquals(5, properties.getWeights().get(Symbol.LEMON));
        assertEquals(1, properties.getWeights().get(Symbol.SEVEN));
    }

    @Test
    void shouldBindPayoutConfiguration() {
        assertNotNull(properties.getPayout());

        assertEquals(
                8,
                properties.getPayout()
                        .get(ResultPattern.THREE_OF_A_KIND)
                        .get(Symbol.CHERRY)
        );

        assertEquals(
                1500,
                properties.getPayout()
                        .get(ResultPattern.THREE_OF_A_KIND)
                        .get(Symbol.SEVEN)
        );

        assertEquals(
                2,
                properties.getPayout()
                        .get(ResultPattern.ONE_OF_A_KIND)
                        .get(Symbol.SEVEN)
        );
    }

    @Test
    void shouldCreateRequiredSlotBeans() {
        assertNotNull(cashOutMultiplier);
        assertNotNull(rule);
        assertNotNull(slotEngine);
    }

    @Test
    void shouldUseConfiguredPayoutInCashOutMultiplier() {
        BigDecimal multiplier =
                cashOutMultiplier.getMultiplierForPattern(
                        ResultPattern.THREE_OF_A_KIND,
                        Symbol.SEVEN
                );

        assertEquals(
                0,
                new BigDecimal("1500").compareTo(multiplier)
        );
    }
}