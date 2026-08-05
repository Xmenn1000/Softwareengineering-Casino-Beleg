package casino.slots;

import casino.slots.domain.dto.GameResult;
import casino.slots.domain.dto.OutCome;
import casino.slots.domain.enums.Symbol;
import casino.slots.domain.machine.CashOutMultiplier;
import casino.slots.domain.machine.SlotMachine;
import casino.slots.domain.ruleSet.Rule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SlotMachineTest {

    @Test
    void shouldReturnWinningResultWhenPayoutIsPositive() {
        List<Symbol> symbols = List.of(
                Symbol.SEVEN,
                Symbol.SEVEN,
                Symbol.SEVEN
        );

        SlotMachine machine = createMachine(
                new BigDecimal("50.00"),
                symbols
        );

        GameResult result =
                machine.play(new BigDecimal("10.00"));

        assertTrue(result.isWinning());

        assertEquals(
                0,
                result.getAmount()
                        .compareTo(new BigDecimal("50.00"))
        );

        assertEquals(symbols, result.getSlotStates());
    }

    @Test
    void shouldSubtractBetAmountWhenPlayerLoses() {
        List<Symbol> symbols = List.of(
                Symbol.CHERRY,
                Symbol.LEMON,
                Symbol.ORANGE
        );

        SlotMachine machine = createMachine(
                BigDecimal.ZERO,
                symbols
        );

        GameResult result =
                machine.play(new BigDecimal("10.00"));

        assertFalse(result.isWinning());

        assertEquals(
                0,
                result.getAmount()
                        .compareTo(new BigDecimal("-10.00"))
        );

        assertEquals(symbols, result.getSlotStates());
    }

    private SlotMachine createMachine(
            BigDecimal fixedPayout,
            List<Symbol> fixedSymbols
    ) {
        Rule fixedRule = (outcome, betAmount) -> fixedPayout;

        return new SlotMachine(
                new CashOutMultiplier(Map.of()),
                0,
                Map.of(),
                fixedRule
        ) {
            @Override
            public OutCome spin() {
                return new OutCome(fixedSymbols);
            }
        };
    }
}