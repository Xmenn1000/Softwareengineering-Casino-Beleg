package casino.slots.service;

import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.SlotsGameNotFoundException;
import casino.slots.model.SlotsGameEntity;
import casino.slots.model.SlotsGameEntityFactory;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.view.SlotsGameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameHistoryServiceImplTest {

    @Mock
    private SlotsGameRepository repository;

    private GameHistoryServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GameHistoryServiceImpl(repository);
    }

    @Test
    void shouldReturnAllGamesAsDTOs() {
        SlotsGameEntity firstGame = createGame(
                1L,
                true,
                "20.00",
                "5.00"
        );

        SlotsGameEntity secondGame = createGame(
                2L,
                false,
                "-10.00",
                "10.00"
        );

        when(repository.findAll())
                .thenReturn(List.of(firstGame, secondGame));

        List<SlotsGameDTO> result = service.findAll();

        assertEquals(2, result.size());

        SlotsGameDTO firstDTO = result.getFirst();

        assertEquals(1L, firstDTO.getUserId());
        assertTrue(firstDTO.isWinning());
        assertBigDecimalEquals("20.00", firstDTO.getAmount());
        assertBigDecimalEquals("5.00", firstDTO.getBetAmount());

        SlotsGameDTO secondDTO = result.get(1);

        assertEquals(2L, secondDTO.getUserId());
        assertFalse(secondDTO.isWinning());
        assertBigDecimalEquals("-10.00", secondDTO.getAmount());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnEmptyListWhenNoGamesExist() {
        when(repository.findAll()).thenReturn(List.of());

        List<SlotsGameDTO> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnGameById() {
        Long gameId = 5L;
        SlotsGameEntity game = createGame(
                1L,
                true,
                "20.00",
                "5.00"
        );

        game.setId(gameId);

        when(repository.findById(gameId))
                .thenReturn(Optional.of(game));

        SlotsGameDTO result = service.findById(gameId);

        assertEquals(gameId, result.getId());
        assertEquals(1L, result.getUserId());
        assertTrue(result.isWinning());
        assertEquals(game.getSlotStates(), result.getSlotStates());

        verify(repository).findById(gameId);
    }

    @Test
    void shouldThrowWhenGameDoesNotExist() {
        Long gameId = 99L;

        when(repository.findById(gameId))
                .thenReturn(Optional.empty());

        SlotsGameNotFoundException exception = assertThrows(
                SlotsGameNotFoundException.class,
                () -> service.findById(gameId)
        );

        assertEquals(
                "Slots game with id 99 not found",
                exception.getMessage()
        );

        verify(repository).findById(gameId);
    }

    @Test
    void shouldDeleteAndReturnGame() {
        Long gameId = 7L;
        SlotsGameEntity game = createGame(
                1L,
                false,
                "-10.00",
                "10.00"
        );

        game.setId(gameId);

        when(repository.findById(gameId))
                .thenReturn(Optional.of(game));

        SlotsGameDTO result = service.deleteById(gameId);

        assertEquals(gameId, result.getId());
        assertEquals(1L, result.getUserId());

        InOrder order = inOrder(repository);

        order.verify(repository).findById(gameId);
        order.verify(repository).delete(game);
    }

    @Test
    void shouldNotDeleteUnknownGame() {
        Long gameId = 99L;

        when(repository.findById(gameId))
                .thenReturn(Optional.empty());

        assertThrows(
                SlotsGameNotFoundException.class,
                () -> service.deleteById(gameId)
        );

        verify(repository).findById(gameId);
        verify(repository, never())
                .delete(any(SlotsGameEntity.class));
    }

    private SlotsGameEntity createGame(
            Long userId,
            boolean winning,
            String amount,
            String betAmount
    ) {
        return SlotsGameEntityFactory.create(
                userId,
                winning,
                new BigDecimal(amount),
                new BigDecimal(betAmount),
                List.of(
                        Symbol.CHERRY,
                        Symbol.LEMON,
                        Symbol.ORANGE
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