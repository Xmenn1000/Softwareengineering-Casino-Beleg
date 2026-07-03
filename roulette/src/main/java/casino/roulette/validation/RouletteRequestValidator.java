package casino.roulette.validation;

import casino.roulette.config.RouletteProperties;
import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.request.RoulettePlayRequestDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class RouletteRequestValidator {

    private final RouletteProperties rouletteProperties;

    public RouletteRequestValidator(RouletteProperties rouletteProperties) {
        this.rouletteProperties = rouletteProperties;
    }

    public void validatePlayRequest(RoulettePlayRequestDTO request) {
        if (request == null) {
            throw new BadRouletteRequestException("Request body must not be empty");
        }

        validateUserId(request.getUser());
        validateBetType(request);
        validateAmount(request.getAmount());
    }

    public void validateUserId(Long userId) {
        if (userId == null) {
            throw new BadRouletteRequestException("User must not be empty");
        }

        if (userId <= 0) {
            throw new BadRouletteRequestException("User must be greater than 0");
        }
    }

    private void validateBetType(RoulettePlayRequestDTO request) {
        if (request.getBetType() == null) {
            throw new BadRouletteRequestException("Bet type must not be empty");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new BadRouletteRequestException("Amount must not be empty");
        }

        BigDecimal minAmount = rouletteProperties.getBetting().getMinAmount();
        BigDecimal maxAmount = rouletteProperties.getBetting().getMaxAmount();

        if (amount.compareTo(minAmount) < 0) {
            throw new BadRouletteRequestException("Amount must be at least " + minAmount);
        }

        if (amount.compareTo(maxAmount) > 0) {
            throw new BadRouletteRequestException("Amount must not be greater than " + maxAmount);
        }
    }
}
