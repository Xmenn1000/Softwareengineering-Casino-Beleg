package casino.slots.domain.ruleSet;

import casino.slots.domain.dto.OutCome;
import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;
import casino.slots.domain.machine.CashOutMultiplier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExactCountRuleTest {

    @Test
    void shouldPayForThreeCherries() {
        CashOutMultiplier multiplier = createMultiplier(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.CHERRY,
                8
        );

        ExactCountRule rule = new ExactCountRule(
                3,
                ResultPattern.THREE_OF_A_KIND,
                multiplier
        );

        OutCome outcome = new OutCome(List.of(
                Symbol.CHERRY,
                Symbol.CHERRY,
                Symbol.CHERRY
        ));

        BigDecimal payout = rule.payOut(
                outcome,
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("80.00", payout);
    }

    @Test
    void shouldReturnZeroWhenExactCountDoesNotMatch() {
        CashOutMultiplier multiplier = createMultiplier(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.CHERRY,
                8
        );

        ExactCountRule rule = new ExactCountRule(
                3,
                ResultPattern.THREE_OF_A_KIND,
                multiplier
        );

        OutCome outcome = new OutCome(List.of(
                Symbol.CHERRY,
                Symbol.CHERRY,
                Symbol.LEMON
        ));

        BigDecimal payout = rule.payOut(
                outcome,
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("0", payout);
    }

    @Test
    void shouldReturnZeroForUnconfiguredSymbol() {
        CashOutMultiplier multiplier = createMultiplier(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.CHERRY,
                8
        );

        ExactCountRule rule = new ExactCountRule(
                3,
                ResultPattern.THREE_OF_A_KIND,
                multiplier
        );

        OutCome outcome = new OutCome(List.of(
                Symbol.LEMON,
                Symbol.LEMON,
                Symbol.LEMON
        ));

        BigDecimal payout = rule.payOut(
                outcome,
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("0", payout);
    }

    @Test
    void shouldPayForOneSeven() {
        CashOutMultiplier multiplier = createMultiplier(
                ResultPattern.ONE_OF_A_KIND,
                Symbol.SEVEN,
                2
        );

        ExactCountRule rule = new ExactCountRule(
                1,
                ResultPattern.ONE_OF_A_KIND,
                multiplier
        );

        OutCome outcome = new OutCome(List.of(
                Symbol.SEVEN,
                Symbol.CHERRY,
                Symbol.LEMON
        ));

        BigDecimal payout = rule.payOut(
                outcome,
                new BigDecimal("10.00")
        );

        assertBigDecimalEquals("20.00", payout);
    }

    private CashOutMultiplier createMultiplier(
            ResultPattern pattern,
            Symbol symbol,
            int multiplier
    ) {
        Map<ResultPattern, Map<Symbol, Integer>> payouts = Map.of(
                pattern,
                Map.of(symbol, multiplier)
        );

        return new CashOutMultiplier(payouts);
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