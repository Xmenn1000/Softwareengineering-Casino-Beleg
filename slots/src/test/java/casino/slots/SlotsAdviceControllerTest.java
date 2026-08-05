package casino.slots;

import casino.slots.controller.SlotsAdviceController;
import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.exeptions.BankingUserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlotsAdviceControllerTest {

    private SlotsAdviceController adviceController;

    @BeforeEach
    void setUp() {
        adviceController = new SlotsAdviceController();
    }

    @Test
    void shouldConvertBadSlotsRequestToBadRequest() {
        BadSlotsRequestException exception =
                new BadSlotsRequestException(
                        "Bet amount must be greater than zero"
                );

        ResponseEntity<ProblemDetail> response =
                adviceController.handle(exception);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode()
        );

        ProblemDetail body = response.getBody();

        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals(
                "Bet amount must be greater than zero",
                body.getDetail()
        );
    }

    @Test
    void shouldConvertMissingBankingUserToNotFound() {
        BankingUserNotFoundException exception =
                new BankingUserNotFoundException(99L);

        ResponseEntity<ProblemDetail> response =
                adviceController.handle(exception);

        assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode()
        );

        ProblemDetail body = response.getBody();

        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals(
                "Banking user with id 99 not found",
                body.getDetail()
        );
    }
}