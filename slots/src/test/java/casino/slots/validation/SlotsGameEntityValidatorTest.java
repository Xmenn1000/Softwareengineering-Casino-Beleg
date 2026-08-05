package casino.slots.validation;

import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.model.SlotsGameEntity;
import casino.slots.model.SlotsGameEntityFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlotsGameEntityValidatorTest {

    private final SlotsGameEntityValidator validator =
            new SlotsGameEntityValidator();

    @Test
    void shouldAcceptValidEntity() {
        SlotsGameEntity entity = createValidEntity();

        assertDoesNotThrow(() -> validator.validate(entity));
    }

    @Test
    void shouldRejectNullAmount() {
        SlotsGameEntity entity = createValidEntity();
        entity.setAmount(null);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    @Test
    void shouldRejectNullBetAmount() {
        SlotsGameEntity entity = createValidEntity();
        entity.setBetAmount(null);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    @Test
    void shouldRejectZeroBetAmount() {
        SlotsGameEntity entity = createValidEntity();
        entity.setBetAmount(BigDecimal.ZERO);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    @Test
    void shouldRejectNullSymbols() {
        SlotsGameEntity entity = createValidEntity();
        entity.setSlotStates(null);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    @Test
    void shouldRejectEmptySymbols() {
        SlotsGameEntity entity = createValidEntity();
        entity.setSlotStates(List.of());

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    @Test
    void shouldRejectUserIdZero() {
        SlotsGameEntity entity = createValidEntity();
        entity.setUserId(0L);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    @Test
    void shouldRejectNegativeUserId() {
        SlotsGameEntity entity = createValidEntity();
        entity.setUserId(-1L);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validate(entity)
        );
    }

    private SlotsGameEntity createValidEntity() {
        return SlotsGameEntityFactory.create(
                1L,
                true,
                new BigDecimal("20.00"),
                new BigDecimal("5.00"),
                List.of(
                        Symbol.CHERRY,
                        Symbol.CHERRY,
                        Symbol.CHERRY
                )
        );
    }
}