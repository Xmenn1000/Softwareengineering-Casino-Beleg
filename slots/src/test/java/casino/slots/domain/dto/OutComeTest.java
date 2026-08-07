package casino.slots.domain.dto;

import casino.slots.domain.enums.Symbol;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutComeTest {

    @Test
    void shouldReturnTheSymbolsItWasBuiltWith() {
        List<Symbol> symbols = List.of(Symbol.CHERRY, Symbol.LEMON, Symbol.SEVEN);

        OutCome outCome = new OutCome(symbols);

        assertEquals(symbols, outCome.getOutCome());
    }

    @Test
    void shouldCountEveryOccurrenceOfASymbol() {
        OutCome outCome = new OutCome(
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.LEMON)
        );

        assertEquals(2, outCome.numberOfSymbols(Symbol.CHERRY));
    }

    @Test
    void shouldCountZeroForASymbolThatDidNotLand() {
        OutCome outCome = new OutCome(
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.LEMON)
        );

        assertEquals(0, outCome.numberOfSymbols(Symbol.SEVEN));
    }

    @Test
    void shouldCountAllReelsWhenEverySymbolMatches() {
        OutCome outCome = new OutCome(
                List.of(Symbol.SEVEN, Symbol.SEVEN, Symbol.SEVEN)
        );

        assertEquals(3, outCome.numberOfSymbols(Symbol.SEVEN));
    }

    @Test
    void shouldReportAllSameSymbolWhenEveryReelMatches() {
        OutCome outCome = new OutCome(
                List.of(Symbol.SEVEN, Symbol.SEVEN, Symbol.SEVEN)
        );

        assertTrue(outCome.isAllSameSymbol(Symbol.SEVEN));
    }

    @Test
    void shouldNotReportAllSameSymbolWhenOneReelDiffers() {
        OutCome outCome = new OutCome(
                List.of(Symbol.SEVEN, Symbol.SEVEN, Symbol.CHERRY)
        );

        assertFalse(outCome.isAllSameSymbol(Symbol.SEVEN));
    }

    @Test
    void shouldNotReportAllSameSymbolForASymbolThatDidNotLand() {
        OutCome outCome = new OutCome(
                List.of(Symbol.SEVEN, Symbol.SEVEN, Symbol.SEVEN)
        );

        assertFalse(outCome.isAllSameSymbol(Symbol.CHERRY));
    }
}
