package casino.slots.controller;

import casino.slots.service.StatsService;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatsController.class)
class StatsControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatsService statsService;

    @Test
    void shouldReturnOverallStats() throws Exception {
        SlotsStatsDTO stats = new SlotsStatsDTO(
                2L,
                5L,
                new BigDecimal("30.00"),
                new BigDecimal("50.00"),
                new BigDecimal("100.00")
        );

        when(statsService.getStats())
                .thenReturn(stats);

        mockMvc.perform(
                        get("/casino/slots/api/stats")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.totalClientCount").value(2))
                .andExpect(jsonPath("$.totalGamesCount").value(5))
                .andExpect(jsonPath("$.totalProfit").value(30.0))
                .andExpect(jsonPath("$.totalCashOut").value(50.0))
                .andExpect(jsonPath("$.totalTurnover").value(100.0));
    }

    @Test
    void shouldReturnStatsForSpecificUser() throws Exception {
        Long userId = 7L;

        SlotsStatsUserDTO stats = new SlotsStatsUserDTO(
                userId,
                3L,
                new BigDecimal("40.00"),
                new BigDecimal("10.00"),
                new BigDecimal("30.00"),
                new BigDecimal("25.00"),
                new BigDecimal("-30.00")
        );

        when(statsService.getStatsByUserId(userId))
                .thenReturn(stats);

        mockMvc.perform(
                        get(
                                "/casino/slots/api/stats/user/{userId}",
                                userId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(jsonPath("$.client").value(7))
                .andExpect(jsonPath("$.totalGamesCount").value(3))
                .andExpect(jsonPath("$.totalWinnings").value(40.0))
                .andExpect(jsonPath("$.totalLosses").value(10.0))
                .andExpect(jsonPath("$.totalClientProfit").value(30.0))
                .andExpect(
                        jsonPath("$.totalHouseTurnoverFromClient")
                                .value(25.0)
                )
                .andExpect(
                        jsonPath("$.totalHouseProfitFromClient")
                                .value(-30.0)
                );
    }
}