package casino.slots.repository;

import casino.slots.domain.enums.Symbol;
import casino.slots.model.SlotsGameEntity;
import casino.slots.model.SlotsGameEntityFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SlotsGameRepositoryTest {

    @Autowired
    private SlotsGameRepository repository;

    @Test
    void shouldSaveAndLoadGame() {
        SlotsGameEntity game = createGame(
                1L,
                true,
                "40.00",
                "10.00",
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                )
        );

        SlotsGameEntity savedGame = repository.saveAndFlush(game);

        assertTrue(savedGame.getId() > 0);

        SlotsGameEntity loadedGame = repository
                .findById(savedGame.getId())
                .orElseThrow();

        assertEquals(1L, loadedGame.getUserId());
        assertTrue(loadedGame.isWinning());

        assertBigDecimalEquals(
                "40.00",
                loadedGame.getAmount()
        );

        assertBigDecimalEquals(
                "10.00",
                loadedGame.getBetAmount()
        );

        assertEquals(
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                ),
                loadedGame.getSlotStates()
        );
    }

    @Test
    void shouldFindOnlyGamesOfRequestedUser() {
        repository.save(createGame(
                1L,
                false,
                "-10.00",
                "10.00",
                List.of(
                        Symbol.CHERRY,
                        Symbol.LEMON,
                        Symbol.ORANGE
                )
        ));

        repository.save(createGame(
                1L,
                true,
                "20.00",
                "5.00",
                List.of(
                        Symbol.CHERRY,
                        Symbol.CHERRY,
                        Symbol.CHERRY
                )
        ));

        repository.save(createGame(
                2L,
                false,
                "-7.00",
                "7.00",
                List.of(
                        Symbol.LEMON,
                        Symbol.ORANGE,
                        Symbol.CHERRY
                )
        ));

        repository.flush();

        List<SlotsGameEntity> userGames =
                repository.findByUserId(1L);

        assertEquals(2, userGames.size());

        assertTrue(
                userGames.stream()
                        .allMatch(game -> game.getUserId() == 1L)
        );
    }

    @Test
    void shouldReturnEmptyListForUnknownUser() {
        List<SlotsGameEntity> games =
                repository.findByUserId(999L);

        assertNotNull(games);
        assertTrue(games.isEmpty());
    }

    private SlotsGameEntity createGame(
            Long userId,
            boolean winning,
            String amount,
            String betAmount,
            List<Symbol> symbols
    ) {
        return SlotsGameEntityFactory.create(
                userId,
                winning,
                new BigDecimal(amount),
                new BigDecimal(betAmount),
                symbols
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