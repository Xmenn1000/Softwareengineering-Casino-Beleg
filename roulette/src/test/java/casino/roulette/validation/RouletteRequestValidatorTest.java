package casino.roulette.validation;

import casino.roulette.config.RouletteProperties;
import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.util.BetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouletteRequestValidatorTest {

    private RouletteRequestValidator validator;

    @BeforeEach
    void setUp() {
        RouletteProperties properties = new RouletteProperties();
        properties.getBetting().setMinAmount(new BigDecimal("1.00"));
        properties.getBetting().setMaxAmount(new BigDecimal("1000.00"));
        validator = new RouletteRequestValidator(properties);
    }

    @Test
    void acceptsValidPlayRequest() {
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                BetType.COLOR,
                "RED",
                new BigDecimal("10.00")
        );

        assertDoesNotThrow(() -> validator.validatePlayRequest(request));
    }

    @Test
    void rejectsEmptyRequest() {
        assertThrows(BadRouletteRequestException.class, () -> validator.validatePlayRequest(null));
    }

    @Test
    void rejectsInvalidUserId() {
        assertThrows(BadRouletteRequestException.class, () -> validator.validateUserId(null));
        assertThrows(BadRouletteRequestException.class, () -> validator.validateUserId(0L));
        assertThrows(BadRouletteRequestException.class, () -> validator.validateUserId(-1L));
    }

    @Test
    void rejectsMissingBetType() {
        RoulettePlayRequestDTO request = new RoulettePlayRequestDTO(
                1L,
                null,
                "RED",
                new BigDecimal("10.00")
        );

        assertThrows(BadRouletteRequestException.class, () -> validator.validatePlayRequest(request));
    }

    @Test
    void rejectsMissingOrOutOfRangeAmount() {
        assertThrows(BadRouletteRequestException.class, () -> validator.validatePlayRequest(
                new RoulettePlayRequestDTO(1L, BetType.COLOR, "RED", null)
        ));
        assertThrows(BadRouletteRequestException.class, () -> validator.validatePlayRequest(
                new RoulettePlayRequestDTO(1L, BetType.COLOR, "RED", new BigDecimal("0.99"))
        ));
        assertThrows(BadRouletteRequestException.class, () -> validator.validatePlayRequest(
                new RoulettePlayRequestDTO(1L, BetType.COLOR, "RED", new BigDecimal("1000.01"))
        ));
    }
}
