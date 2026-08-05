package casino.banking.controller.transaction;

import casino.banking.exceptions.HttpException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionsAdviceControllerTest {

    @ParameterizedTest
    @EnumSource(value = HttpStatus.class, names = {"NOT_FOUND", "BAD_REQUEST", "INTERNAL_SERVER_ERROR"})
    void handle_mapsStatusAndMessageFaithfully(HttpStatus status) {
        String payload = "detail-" + status.value();

        HttpException exceptionMock = mock(HttpException.class);
        when(exceptionMock.getHTTPStatus()).thenReturn(status);
        when(exceptionMock.getMessage()).thenReturn(payload);

        TransactionsAdviceController adviceController = new TransactionsAdviceController();

        ResponseEntity<ProblemDetail> result = adviceController.handle(exceptionMock);

        assertEquals(status, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(status.value(), result.getBody().getStatus());
        assertEquals(payload, result.getBody().getDetail());
    }
}
