package casino.roulette.mapper;

import casino.roulette.model.RouletteGameEntity;
import casino.roulette.util.BetType;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouletteGameMapperTest {

    @Test
    void mapsEntityToGameDto() {
        RouletteGameEntity entity = entity();

        RouletteGameDTO result = RouletteGameMapper.toGameDto(entity);

        assertEquals(entity.getId(), result.getId());
        assertEquals(1L, result.getUser());
        assertTrue(result.isWinning());
        assertEquals(0, new BigDecimal("35.00").compareTo(result.getAmount()));
        assertEquals(0, new BigDecimal("1.00").compareTo(result.getBetAmount()));
        assertEquals(BetType.STRAIGHT_NUMBER, result.getBetType());
        assertEquals("17", result.getBetValue());
        assertEquals(17, result.getBallPosition());
    }

    @Test
    void mapsEntityToPlayResultDto() {
        RouletteGameEntity entity = entity();

        RoulettePlayResultDTO result = RouletteGameMapper.toPlayResultDto(entity);

        assertEquals(1L, result.getUser());
        assertTrue(result.isWinning());
        assertEquals(0, new BigDecimal("35.00").compareTo(result.getAmount()));
        assertEquals(0, new BigDecimal("1.00").compareTo(result.getBetAmount()));
        assertEquals(BetType.STRAIGHT_NUMBER, result.getBetType());
        assertEquals("17", result.getBetValue());
        assertEquals(17, result.getBallPosition());
    }

    private RouletteGameEntity entity() {
        return RouletteGameEntity.create(
                1L,
                true,
                new BigDecimal("35.00"),
                new BigDecimal("1.00"),
                BetType.STRAIGHT_NUMBER,
                "17",
                17
        );
    }
}
