package casino.slots;

import casino.slots.controller.PlayController;
import casino.slots.domain.enums.Symbol;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.service.PlayService;
import casino.slots.view.SlotsGameResultDTO;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayControllerTest {

    @Mock
    private PlayService playService;

    private PlayController playController;

    @BeforeEach
    void setUp() {
        playController = new PlayController(playService);
    }

    @Test
    void shouldReturnPlayResultWithStatusOk() {
        SlotsPlayRequest request = new SlotsPlayRequest();
        request.setUserId(1L);
        request.setBetAmount(new BigDecimal("10.00"));

        SlotsGameResultDTO expectedResult =
                new SlotsGameResultDTO(
                        1L,
                        true,
                        new BigDecimal("40.00"),
                        List.of(
                                Symbol.SEVEN,
                                Symbol.SEVEN,
                                Symbol.SEVEN
                        ),
                        new BigDecimal("10.00")
                );

        when(playService.play(request))
                .thenReturn(expectedResult);

        ResponseEntity<SlotsGameResultDTO> response =
                playController.requestPlay(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expectedResult, response.getBody());

        verify(playService).play(request);
        verifyNoMoreInteractions(playService);
    }
}