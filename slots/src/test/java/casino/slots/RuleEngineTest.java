package casino.slots;

import casino.slots.domain.dto.OutCome;
import casino.slots.domain.ruleSet.Rule;
import casino.slots.domain.ruleSet.RuleEngine;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RuleEngineTest {

    @Test
    void shouldReturnHighestPayout() {
        Rule lowPayoutRule = (outcome, betAmount) ->
                new BigDecimal("10.00");

        Rule highestPayoutRule = (outcome, betAmount) ->
                new BigDecimal("50.00");

        Rule mediumPayoutRule = (outcome, betAmount) ->
                new BigDecimal("25.00");

        RuleEngine ruleEngine = new RuleEngine(
                List.of(
                        lowPayoutRule,
                        highestPayoutRule,
                        mediumPayoutRule
                )
        );

        BigDecimal result = ruleEngine.payOut(
                new OutCome(List.of()),
                new BigDecimal("5.00")
        );

        assertBigDecimalEquals("50.00", result);
    }

    @Test
    void shouldReturnZeroWhenNoRulePays() {
        Rule firstRule = (outcome, betAmount) ->
                BigDecimal.ZERO;

        Rule secondRule = (outcome, betAmount) ->
                BigDecimal.ZERO;

        RuleEngine ruleEngine = new RuleEngine(
                List.of(firstRule, secondRule)
        );

        BigDecimal result = ruleEngine.payOut(
                new OutCome(List.of()),
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("0", result);
    }

    @Test
    void shouldReturnZeroWhenRuleListIsEmpty() {
        RuleEngine ruleEngine = new RuleEngine(List.of());

        BigDecimal result = ruleEngine.payOut(
                new OutCome(List.of()),
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("0", result);
    }

    @Test
    void shouldIgnoreNegativePayouts() {
        Rule negativeRule = (outcome, betAmount) ->
                new BigDecimal("-10.00");

        RuleEngine ruleEngine = new RuleEngine(
                List.of(negativeRule)
        );

        BigDecimal result = ruleEngine.payOut(
                new OutCome(List.of()),
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("0", result);
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