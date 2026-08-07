package casino.slots.mapper;

import casino.slots.domain.enums.Symbol;
import casino.slots.model.SlotsGameEntity;
import casino.slots.model.SlotsGameEntityFactory;
import casino.slots.view.SlotsGameDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlotsGameMapperTest {

    @Test
    void shouldMapEntityToDto() {
        SlotsGameEntity entity = createValidEntity();
        entity.setId(42L);

        SlotsGameDTO result = SlotsGameMapper.toDTO(entity);

        assertEquals(42L, result.getId());
        assertEquals(1L, result.getUser());
        assertTrue(result.isWinning());
        assertEquals(0, new BigDecimal("20.00").compareTo(result.getAmount()));
        assertEquals(0, new BigDecimal("5.00").compareTo(result.getBetAmount()));
        assertEquals(
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.CHERRY),
                result.getSlotStates()
        );
    }

    @Test
    void shouldMapLosingGameWithNegativeAmount() {
        SlotsGameEntity entity = SlotsGameEntityFactory.create(
                7L,
                false,
                new BigDecimal("-5.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.CHERRY, Symbol.LEMON, Symbol.ORANGE)
        );

        SlotsGameDTO result = SlotsGameMapper.toDTO(entity);

        assertEquals(7L, result.getUser());
        assertFalse(result.isWinning());
        assertEquals(0, new BigDecimal("-5.00").compareTo(result.getAmount()));
    }

    @Test
    void shouldKeepSymbolOrderOfTheReels() {
        SlotsGameEntity entity = createValidEntity();
        entity.setSlotStates(List.of(Symbol.SEVEN, Symbol.CHERRY, Symbol.LEMON));

        SlotsGameDTO result = SlotsGameMapper.toDTO(entity);

        assertEquals(
                List.of(Symbol.SEVEN, Symbol.CHERRY, Symbol.LEMON),
                result.getSlotStates()
        );
    }

    private SlotsGameEntity createValidEntity() {
        return SlotsGameEntityFactory.create(
                1L,
                true,
                new BigDecimal("20.00"),
                new BigDecimal("5.00"),
                List.of(Symbol.CHERRY, Symbol.CHERRY, Symbol.CHERRY)
        );
    }
}
