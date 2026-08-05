package casino.roulette.controller;

import casino.roulette.exceptions.BadRouletteRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RouletteAdviceControllerTest {

    @Test
    void handleMapsHttpExceptionToProblemDetailResponse() {
        RouletteAdviceController adviceController = new RouletteAdviceController();

        ResponseEntity<ProblemDetail> response = adviceController.handle(
                new BadRouletteRequestException("Invalid roulette request")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid roulette request", response.getBody().getDetail());
    }
}
