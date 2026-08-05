package casino.slots.controller;

import casino.slots.service.GameHistoryService;
import casino.slots.view.SlotsGameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameHistoryControllerTest {

    @Mock
    private GameHistoryService gameHistoryService;

    private GameHistoryController controller;

    @BeforeEach
    void setUp() {
        controller = new GameHistoryController(gameHistoryService);
    }

    @Test
    void shouldReturnAllGamesWithStatusOk() {
        SlotsGameDTO firstGame = mock(SlotsGameDTO.class);
        SlotsGameDTO secondGame = mock(SlotsGameDTO.class);

        List<SlotsGameDTO> expectedGames =
                List.of(firstGame, secondGame);

        when(gameHistoryService.findAll())
                .thenReturn(expectedGames);

        ResponseEntity<List<SlotsGameDTO>> response =
                controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedGames, response.getBody());

        verify(gameHistoryService).findAll();
        verifyNoMoreInteractions(gameHistoryService);
    }

    @Test
    void shouldReturnGameByIdWithStatusOk() {
        Long gameId = 5L;
        SlotsGameDTO expectedGame = mock(SlotsGameDTO.class);

        when(gameHistoryService.findById(gameId))
                .thenReturn(expectedGame);

        ResponseEntity<SlotsGameDTO> response =
                controller.findById(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedGame, response.getBody());

        verify(gameHistoryService).findById(gameId);
        verifyNoMoreInteractions(gameHistoryService);
    }

    @Test
    void shouldDeleteGameByIdWithStatusOk() {
        Long gameId = 5L;
        SlotsGameDTO deletedGame = mock(SlotsGameDTO.class);

        when(gameHistoryService.deleteById(gameId))
                .thenReturn(deletedGame);

        ResponseEntity<SlotsGameDTO> response =
                controller.deleteById(gameId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(deletedGame, response.getBody());

        verify(gameHistoryService).deleteById(gameId);
        verifyNoMoreInteractions(gameHistoryService);
    }
}