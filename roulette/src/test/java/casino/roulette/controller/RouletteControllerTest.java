package casino.roulette.controller;

import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.service.RouletteService;
import casino.roulette.util.BetType;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouletteControllerTest {

    @Mock
    private RouletteService rouletteService;

    private RouletteController rouletteController;

    @BeforeEach
    void setUp() {
        rouletteController = new RouletteController(rouletteService);
    }

    @Test
    void playDelegatesToServiceAndReturnsOk() {
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                BetType.COLOR,
                "RED",
                new BigDecimal("10.00")
        );
        RoulettePlayResultDTO response = new RoulettePlayResultDTO(
                1L,
                true,
                new BigDecimal("10.00"),
                new BigDecimal("10.00"),
                BetType.COLOR,
                "RED",
                7
        );

        when(rouletteService.play(request)).thenReturn(response);

        ResponseEntity<RoulettePlayResultDTO> result = rouletteController.play(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertSame(response, result.getBody());
        verify(rouletteService).play(request);
    }

    @Test
    void infoEndpointsDelegateToService() {
        when(rouletteService.getRules()).thenReturn("rules");
        when(rouletteService.getChances()).thenReturn("chances");

        assertEquals("rules", rouletteController.getRules().getBody());
        assertEquals("chances", rouletteController.getChances().getBody());
    }

    @Test
    void statsEndpointsDelegateToService() {
        RouletteStatsDTO stats = new RouletteStatsDTO(
                1,
                1,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
        RouletteUserStatsDTO userStats = new RouletteUserStatsDTO(
                1L,
                1,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(rouletteService.getStats()).thenReturn(stats);
        when(rouletteService.getUserStats(1L)).thenReturn(userStats);

        assertSame(stats, rouletteController.getStats().getBody());
        assertSame(userStats, rouletteController.getUserStats(1L).getBody());
    }

    @Test
    void gameEndpointsDelegateToService() {
        RouletteGameDTO game = new RouletteGameDTO(
                1L,
                1L,
                true,
                new BigDecimal("10.00"),
                new BigDecimal("10.00"),
                BetType.COLOR,
                "RED",
                7
        );
        List<RouletteGameDTO> games = List.of(game);

        when(rouletteService.getGames()).thenReturn(games);
        when(rouletteService.getGame(1L)).thenReturn(game);
        when(rouletteService.deleteGame(1L)).thenReturn(game);

        assertSame(games, rouletteController.getGames().getBody());
        assertSame(game, rouletteController.getGame(1L).getBody());
        assertSame(game, rouletteController.deleteGame(1L).getBody());
    }
}
