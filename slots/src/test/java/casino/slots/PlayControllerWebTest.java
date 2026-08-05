package casino.slots;

import casino.slots.controller.PlayController;
import casino.slots.controller.SlotsAdviceController;
import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.exeptions.BankingUserNotFoundException;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.service.PlayService;
import casino.slots.view.SlotsGameResultDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayController.class)
@Import(SlotsAdviceController.class)
class PlayControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayService playService;

    @Test
    void shouldReturnPlayResultForValidRequest() throws Exception {
        SlotsGameResultDTO result = new SlotsGameResultDTO(
                7L,
                true,
                new BigDecimal("40.00"),
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                ),
                new BigDecimal("10.00")
        );

        when(playService.play(any(SlotsPlayRequest.class)))
                .thenReturn(result);

        mockMvc.perform(
                        post("/casino/slots/api/play")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "userId": 7,
                                          "betAmount": 10.00
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andDo(print())
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.winning").value(true))
                .andExpect(jsonPath("$.amount").value(40.00))
                .andExpect(jsonPath("$.betAmount").value(10.00))
                .andExpect(jsonPath("$.slotStates[0]").value("SEVEN"))
                .andExpect(jsonPath("$.slotStates[1]").value("SEVEN"))
                .andExpect(jsonPath("$.slotStates[2]").value("SEVEN"));
    }

    @Test
    void shouldReturnBadRequestForInvalidPlayRequest() throws Exception {
        when(playService.play(any(SlotsPlayRequest.class)))
                .thenThrow(new BadSlotsRequestException(
                        "Bet amount must be greater than zero"
                ));

        mockMvc.perform(
                        post("/casino/slots/api/play")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "userId": 7,
                                          "betAmount": 0
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value(
                        "Bet amount must be greater than zero"
                ));
    }

    @Test
    void shouldReturnNotFoundWhenBankingUserDoesNotExist() throws Exception {
        when(playService.play(any(SlotsPlayRequest.class)))
                .thenThrow(new BankingUserNotFoundException(99L));

        mockMvc.perform(
                        post("/casino/slots/api/play")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "userId": 99,
                                          "betAmount": 10.00
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value(
                        "Banking user with id 99 not found"
                ));
    }
}