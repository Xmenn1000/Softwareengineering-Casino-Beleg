package casino.slots.controller;

import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.SlotsGameNotFoundException;
import casino.slots.service.GameHistoryService;
import casino.slots.view.SlotsGameDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameHistoryController.class)
@Import(SlotsAdviceController.class)
class GameHistoryControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameHistoryService gameHistoryService;

    @Test
    void shouldReturnAllGames() throws Exception {
        SlotsGameDTO firstGame = new SlotsGameDTO(
                10L,
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

        SlotsGameDTO secondGame = new SlotsGameDTO(
                11L,
                2L,
                false,
                new BigDecimal("-10.00"),
                new BigDecimal("10.00"),
                List.of(
                        Symbol.CHERRY,
                        Symbol.LEMON,
                        Symbol.ORANGE
                )
        );

        when(gameHistoryService.findAll())
                .thenReturn(List.of(firstGame, secondGame));

        mockMvc.perform(
                        get("/casino/slots/api/stats/games")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))

                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].winning").value(true))
                .andExpect(jsonPath("$[0].amount").value(20.0))
                .andExpect(jsonPath("$[0].betAmount").value(5.0))
                .andExpect(jsonPath("$[0].slotStates[0]")
                        .value("CHERRY"))

                .andExpect(jsonPath("$[1].id").value(11))
                .andExpect(jsonPath("$[1].userId").value(2))
                .andExpect(jsonPath("$[1].winning").value(false))
                .andExpect(jsonPath("$[1].amount").value(-10.0));
    }

    @Test
    void shouldReturnGameById() throws Exception {
        Long gameId = 5L;

        SlotsGameDTO game = new SlotsGameDTO(
                gameId,
                7L,
                true,
                new BigDecimal("40.00"),
                new BigDecimal("10.00"),
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                )
        );

        when(gameHistoryService.findById(gameId))
                .thenReturn(game);

        mockMvc.perform(
                        get("/casino/slots/api/stat/{gameId}", gameId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.winning").value(true))
                .andExpect(jsonPath("$.amount").value(40.0))
                .andExpect(jsonPath("$.betAmount").value(10.0))
                .andExpect(jsonPath("$.slotStates.length()").value(3))
                .andExpect(jsonPath("$.slotStates[0]")
                        .value("SEVEN"));
    }

    @Test
    void shouldDeleteGameById() throws Exception {
        Long gameId = 5L;

        SlotsGameDTO deletedGame = new SlotsGameDTO(
                gameId,
                7L,
                false,
                new BigDecimal("-10.00"),
                new BigDecimal("10.00"),
                List.of(
                        Symbol.CHERRY,
                        Symbol.LEMON,
                        Symbol.ORANGE
                )
        );

        when(gameHistoryService.deleteById(gameId))
                .thenReturn(deletedGame);

        mockMvc.perform(
                        delete("/casino/slots/api/stat/{gameId}", gameId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.winning").value(false))
                .andExpect(jsonPath("$.amount").value(-10.0));
    }

    @Test
    void shouldReturnNotFoundForUnknownGame() throws Exception {
        Long gameId = 99L;

        when(gameHistoryService.findById(gameId))
                .thenThrow(new SlotsGameNotFoundException(gameId));

        mockMvc.perform(
                        get("/casino/slots/api/stat/{gameId}", gameId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(
                        "Slots game with id 99 not found"
                ));
    }
}