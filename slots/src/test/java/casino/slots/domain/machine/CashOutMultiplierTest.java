package casino.slots.domain.machine;

import casino.slots.domain.enums.ResultPattern;
import casino.slots.domain.enums.Symbol;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CashOutMultiplierTest {

    @Test
    void shouldReturnConfiguredMultiplier() {
        CashOutMultiplier multiplier = createMultiplier();

        BigDecimal result = multiplier.getMultiplierForPattern(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.CHERRY
        );

        assertBigDecimalEquals("8", result);
    }

    @Test
    void shouldReturnDifferentConfiguredMultipliers() {
        CashOutMultiplier multiplier = createMultiplier();

        BigDecimal cherryResult = multiplier.getMultiplierForPattern(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.CHERRY
        );

        BigDecimal sevenResult = multiplier.getMultiplierForPattern(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.SEVEN
        );

        assertBigDecimalEquals("8", cherryResult);
        assertBigDecimalEquals("50", sevenResult);
    }

    @Test
    void shouldReturnZeroForUnknownSymbolInKnownPattern() {
        CashOutMultiplier multiplier = new CashOutMultiplier(
                Map.of(
                        ResultPattern.THREE_OF_A_KIND,
                        Map.of(Symbol.CHERRY, 8)
                )
        );

        BigDecimal result = multiplier.getMultiplierForPattern(
                ResultPattern.THREE_OF_A_KIND,
                Symbol.LEMON
        );

        assertBigDecimalEquals("0", result);
    }

    private CashOutMultiplier createMultiplier() {
        return new CashOutMultiplier(
                Map.of(
                        ResultPattern.THREE_OF_A_KIND,
                        Map.of(
                                Symbol.CHERRY, 8,
                                Symbol.SEVEN, 50
                        ),
                        ResultPattern.ONE_OF_A_KIND,
                        Map.of(
                                Symbol.SEVEN, 2
                        )
                )
        );
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