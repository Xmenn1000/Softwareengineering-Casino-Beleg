package casino.roulette.model;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouletteGameEntityTest {

    @Test
    void createBuildsValidEntityAndNormalizesBetValue() {
        RouletteGameEntity entity = RouletteGameEntity.create(
                1L,
                true,
                new BigDecimal("35.00"),
                new BigDecimal("1.00"),
                BetType.STRAIGHT_NUMBER,
                " 17 ",
                17
        );

        assertEquals(1L, entity.getUser());
        assertTrue(entity.isWinning());
        assertEquals(0, new BigDecimal("35.00").compareTo(entity.getAmount()));
        assertEquals(0, new BigDecimal("1.00").compareTo(entity.getBetAmount()));
        assertEquals(BetType.STRAIGHT_NUMBER, entity.getBetType());
        assertEquals("17", entity.getBetValue());
        assertEquals(17, entity.getBallPosition());
    }

    @Test
    void createRejectsInvalidEntityState() {
        assertThrows(BadRouletteRequestException.class, () -> RouletteGameEntity.create(
                null,
                true,
                new BigDecimal("35.00"),
                new BigDecimal("1.00"),
                BetType.STRAIGHT_NUMBER,
                "17",
                17
        ));
        assertThrows(BadRouletteRequestException.class, () -> RouletteGameEntity.create(
                1L,
                true,
                null,
                new BigDecimal("1.00"),
                BetType.STRAIGHT_NUMBER,
                "17",
                17
        ));
        assertThrows(BadRouletteRequestException.class, () -> RouletteGameEntity.create(
                1L,
                true,
                new BigDecimal("35.00"),
                BigDecimal.ZERO,
                BetType.STRAIGHT_NUMBER,
                "17",
                17
        ));
        assertThrows(BadRouletteRequestException.class, () -> RouletteGameEntity.create(
                1L,
                true,
                new BigDecimal("35.00"),
                new BigDecimal("1.00"),
                null,
                "17",
                17
        ));
        assertThrows(BadRouletteRequestException.class, () -> RouletteGameEntity.create(
                1L,
                true,
                new BigDecimal("35.00"),
                new BigDecimal("1.00"),
                BetType.STRAIGHT_NUMBER,
                " ",
                17
        ));
        assertThrows(BadRouletteRequestException.class, () -> RouletteGameEntity.create(
                1L,
                true,
                new BigDecimal("35.00"),
                new BigDecimal("1.00"),
                BetType.STRAIGHT_NUMBER,
                "17",
                37
        ));
    }
}
