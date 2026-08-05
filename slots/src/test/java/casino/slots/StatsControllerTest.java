package casino.slots;

import casino.slots.controller.StatsController;
import casino.slots.service.StatsService;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsControllerTest {

    @Mock
    private StatsService statsService;

    private StatsController statsController;

    @BeforeEach
    void setUp() {
        statsController = new StatsController(statsService);
    }

    @Test
    void shouldReturnOverallStatsWithStatusOk() {
        SlotsStatsDTO expectedStats = new SlotsStatsDTO(
                2L,
                5L,
                new BigDecimal("30.00"),
                new BigDecimal("50.00"),
                new BigDecimal("100.00")
        );

        when(statsService.getStats())
                .thenReturn(expectedStats);

        ResponseEntity<SlotsStatsDTO> response =
                statsController.getStats();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedStats, response.getBody());

        verify(statsService).getStats();
        verifyNoMoreInteractions(statsService);
    }

    @Test
    void shouldReturnUserStatsWithStatusOk() {
        Long userId = 7L;

        SlotsStatsUserDTO expectedStats =
                new SlotsStatsUserDTO(
                        userId,
                        3L,
                        new BigDecimal("40.00"),
                        new BigDecimal("10.00"),
                        new BigDecimal("30.00"),
                        new BigDecimal("25.00"),
                        new BigDecimal("-30.00")
                );

        when(statsService.getStatsByUserId(userId))
                .thenReturn(expectedStats);

        ResponseEntity<SlotsStatsUserDTO> response =
                statsController.getStatsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedStats, response.getBody());

        verify(statsService).getStatsByUserId(userId);
        verifyNoMoreInteractions(statsService);
    }
}