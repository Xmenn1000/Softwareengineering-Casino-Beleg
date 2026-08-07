package casino.slots.model;

import casino.slots.domain.enums.Symbol;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlotsGameEntityFactoryTest {

    @Test
    void shouldSetAllFields() {
        SlotsGameEntity entity = SlotsGameEntityFactory.create(
                1L,
                true,
                new BigDecimal("20.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.CHERRY)
        );

        assertEquals(1L, entity.getUserId());
        assertTrue(entity.isWinning());
        assertEquals(0, new BigDecimal("20.00").compareTo(entity.getAmount()));
        assertEquals(0, new BigDecimal("5.00").compareTo(entity.getBetAmount()));
        assertEquals(
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.CHERRY),
                entity.getSlotStates()
        );
    }

    @Test
    void shouldLeaveIdUnsetForTheDatabase() {
        SlotsGameEntity entity = SlotsGameEntityFactory.create(
                1L,
                false,
                new BigDecimal("-5.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.LEMON, Symbol.ORANGE, Symbol.PLUM)
        );

        assertEquals(0L, entity.getId());
        assertFalse(entity.isWinning());
    }

    @Test
    void shouldReturnANewInstanceOnEveryCall() {
        SlotsGameEntity first = SlotsGameEntityFactory.create(
                1L,
                true,
                new BigDecimal("20.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.SEVEN, Symbol.SEVEN, Symbol.SEVEN)
        );
        SlotsGameEntity second = SlotsGameEntityFactory.create(
                1L,
                true,
                new BigDecimal("20.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.SEVEN, Symbol.SEVEN, Symbol.SEVEN)
        );

        assertNotSame(first, second);
    }

    @Test
    void shouldNotValidateItsInput() {
        assertDoesNotThrow(() -> SlotsGameEntityFactory.create(
                -1L,
                true,
                null,
                BigDecimal.ZERO,
                List.of()
        ));
    }

    @Test
    void shouldKeepInvalidValuesAsGiven() {
        SlotsGameEntity entity = SlotsGameEntityFactory.create(
                -1L,
                true,
                null,
                BigDecimal.ZERO,
                List.of()
        );

        assertEquals(-1L, entity.getUserId());
        assertNull(entity.getAmount());
        assertEquals(0, BigDecimal.ZERO.compareTo(entity.getBetAmount()));
        assertTrue(entity.getSlotStates().isEmpty());
    }
}
