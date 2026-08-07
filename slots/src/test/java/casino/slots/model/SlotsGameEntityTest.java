package casino.slots.model;

import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.BadSlotsRequestException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlotsGameEntityTest {

    @Test
    void shouldCreateEntityWithAllFields() {
        SlotsGameEntity entity = SlotsGameEntity.create(
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
    void shouldAcceptLosingGameWithNegativeAmount() {
        SlotsGameEntity entity = SlotsGameEntity.create(
                1L,
                false,
                new BigDecimal("-5.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.CHERRY, Symbol.LEMON, Symbol.ORANGE)
        );

        assertFalse(entity.isWinning());
        assertEquals(0, new BigDecimal("-5.00").compareTo(entity.getAmount()));
    }

    @Test
    void shouldLeaveIdUnsetForTheDatabase() {
        SlotsGameEntity entity = SlotsGameEntity.create(
                1L,
                true,
                new BigDecimal("20.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.CHERRY)
        );

        assertEquals(0L, entity.getId());
    }

    @Test
    void shouldRejectUserIdZero() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        0L,
                        true,
                        new BigDecimal("20.00"),
                        new BigDecimal("5.00"),
                        List.of(Symbol.CHERRY)
                )
        );
    }

    @Test
    void shouldRejectNegativeUserId() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        -1L,
                        true,
                        new BigDecimal("20.00"),
                        new BigDecimal("5.00"),
                        List.of(Symbol.CHERRY)
                )
        );
    }

    @Test
    void shouldRejectNullAmount() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        1L,
                        true,
                        null,
                        new BigDecimal("5.00"),
                        List.of(Symbol.CHERRY)
                )
        );
    }

    @Test
    void shouldRejectNullBetAmount() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        1L,
                        true,
                        new BigDecimal("20.00"),
                        null,
                        List.of(Symbol.CHERRY)
                )
        );
    }

    @Test
    void shouldRejectZeroBetAmount() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        1L,
                        true,
                        new BigDecimal("20.00"),
                        BigDecimal.ZERO,
                        List.of(Symbol.CHERRY)
                )
        );
    }

    @Test
    void shouldRejectNegativeBetAmount() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        1L,
                        true,
                        new BigDecimal("20.00"),
                        new BigDecimal("-5.00"),
                        List.of(Symbol.CHERRY)
                )
        );
    }

    @Test
    void shouldRejectNullSlotStates() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        1L,
                        true,
                        new BigDecimal("20.00"),
                        new BigDecimal("5.00"),
                        null
                )
        );
    }

    @Test
    void shouldRejectEmptySlotStates() {
        assertThrows(
                BadSlotsRequestException.class,
                () -> SlotsGameEntity.create(
                        1L,
                        true,
                        new BigDecimal("20.00"),
                        new BigDecimal("5.00"),
                        List.of()
                )
        );
    }
}
