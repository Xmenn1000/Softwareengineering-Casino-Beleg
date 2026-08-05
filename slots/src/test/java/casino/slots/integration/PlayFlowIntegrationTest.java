package casino.slots.integration;

import casino.slots.domain.dto.GameResult;
import casino.slots.domain.enums.Symbol;
import casino.slots.domain.machine.SlotEngine;
import casino.slots.model.SlotsGameEntity;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.restClient.BankingRestClient;
import casino.slots.restClient.BankingUserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:slots-play-flow;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",

        "casino.services.bank.baseURL=http://unused",
        "slots.invoicingParty=SLOTS"
})
class PlayFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SlotsGameRepository repository;

    @MockitoBean
    private SlotEngine slotEngine;

    @MockitoBean
    private BankingRestClient bankingRestClient;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldPlayBookTransactionAndSaveGame() throws Exception {
        BankingUserDTO bankingUser = mock(BankingUserDTO.class);

        when(bankingRestClient.findUserById(7L))
                .thenReturn(bankingUser);

        when(slotEngine.play(any(BigDecimal.class)))
                .thenReturn(new GameResult(
                        true,
                        new BigDecimal("40.00"),
                        List.of(
                                Symbol.SEVEN,
                                Symbol.SEVEN,
                                Symbol.SEVEN
                        )
                ));

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
                .andExpect(jsonPath("$.userId").value(7))
                .andExpect(jsonPath("$.winning").value(true))
                .andExpect(jsonPath("$.amount").value(40.0))
                .andExpect(jsonPath("$.betAmount").value(10.0))
                .andExpect(jsonPath("$.slotStates[0]").value("SEVEN"))
                .andExpect(jsonPath("$.slotStates[1]").value("SEVEN"))
                .andExpect(jsonPath("$.slotStates[2]").value("SEVEN"));

        List<SlotsGameEntity> savedGames = repository.findAll();

        assertEquals(1, savedGames.size());

        SlotsGameEntity savedGame = savedGames.get(0);

        assertEquals(7L, savedGame.getUserId());
        assertTrue(savedGame.isWinning());

        assertBigDecimalEquals(
                "40.00",
                savedGame.getAmount()
        );

        assertBigDecimalEquals(
                "10.00",
                savedGame.getBetAmount()
        );

        assertEquals(
                List.of(
                        Symbol.SEVEN,
                        Symbol.SEVEN,
                        Symbol.SEVEN
                ),
                savedGame.getSlotStates()
        );

        verify(bankingRestClient)
                .findUserById(7L);

        verify(slotEngine)
                .play(argThat(amount ->
                        amount.compareTo(
                                new BigDecimal("10.00")
                        ) == 0
                ));

        verify(bankingRestClient)
                .createSlotsTransaction(
                        eq(7L),
                        argThat(amount ->
                                amount.compareTo(
                                        new BigDecimal("40.00")
                                ) == 0
                        )
                );
    }

    @Test
    void shouldRejectInvalidUserBeforePlaying() throws Exception {
        mockMvc.perform(
                        post("/casino/slots/api/play")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "userId": 0,
                                          "betAmount": 10.00
                                        }
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        assertEquals(0, repository.count());

        verifyNoInteractions(
                slotEngine,
                bankingRestClient
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