package casino.slots;

import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.validation.SlotsRequestValidation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SlotsRequestValidationTest {

    private final SlotsRequestValidation validator =
            new SlotsRequestValidation();

    @Test
    void shouldAcceptValidRequest() {
        SlotsPlayRequest request = createRequest(
                1L,
                new BigDecimal("10.00")
        );

        assertDoesNotThrow(() ->
                validator.validatePlayRequest(request)
        );
    }

    @Test
    void shouldRejectNullRequest() {
        BadSlotsRequestException exception = assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validatePlayRequest(null)
        );

        assertEquals("RequestBody was null", exception.getMessage());
    }

    @Test
    void shouldRejectNullUserId() {
        SlotsPlayRequest request = createRequest(
                null,
                new BigDecimal("10.00")
        );

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validatePlayRequest(request)
        );
    }

    @Test
    void shouldRejectUserIdZero() {
        SlotsPlayRequest request = createRequest(
                0L,
                new BigDecimal("10.00")
        );

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validatePlayRequest(request)
        );
    }

    @Test
    void shouldRejectNullBetAmount() {
        SlotsPlayRequest request = createRequest(1L, null);

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validatePlayRequest(request)
        );
    }

    @Test
    void shouldRejectZeroBetAmount() {
        SlotsPlayRequest request = createRequest(
                1L,
                BigDecimal.ZERO
        );

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validatePlayRequest(request)
        );
    }

    @Test
    void shouldRejectNegativeBetAmount() {
        SlotsPlayRequest request = createRequest(
                1L,
                new BigDecimal("-5.00")
        );

        assertThrows(
                BadSlotsRequestException.class,
                () -> validator.validatePlayRequest(request)
        );
    }

    private SlotsPlayRequest createRequest(
            Long userId,
            BigDecimal betAmount
    ) {
        SlotsPlayRequest request = new SlotsPlayRequest();
        request.setUserId(userId);
        request.setBetAmount(betAmount);
        return request;
    }
}