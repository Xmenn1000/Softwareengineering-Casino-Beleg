package casino.slots;

import casino.slots.controller.InfoController;
import casino.slots.service.InfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfoControllerTest {

    @Mock
    private InfoService infoService;

    private InfoController infoController;

    @BeforeEach
    void setUp() {
        infoController = new InfoController(infoService);
    }

    @Test
    void shouldReturnRulesWithStatusOk() {
        String expectedRules = "Slots - Game Rules";

        when(infoService.getRules())
                .thenReturn(expectedRules);

        ResponseEntity<String> response =
                infoController.getRules();

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertEquals(
                expectedRules,
                response.getBody()
        );

        verify(infoService).getRules();
        verifyNoMoreInteractions(infoService);
    }

    @Test
    void shouldReturnChancesWithStatusOk() {
        String expectedChances =
                "CHERRY: 50 percent";

        when(infoService.getChances())
                .thenReturn(expectedChances);

        ResponseEntity<String> response =
                infoController.getChances();

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertEquals(
                expectedChances,
                response.getBody()
        );

        verify(infoService).getChances();
        verifyNoMoreInteractions(infoService);
    }
}