package casino.slots.validation;

import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.request.SlotsPlayRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SlotsRequestValidation {

    public void validatePlayRequest(SlotsPlayRequest gameRequest) {
        if (gameRequest == null) {
            throw new BadSlotsRequestException("RequestBody was null");
        }

        validateUserId(gameRequest.getUser());
        validateBetAmount(gameRequest.getBetAmount());

    }

    private void validateUserId(Long userId) {
        if (userId == null) {
            throw new BadSlotsRequestException("User Id was null");
        }

        if (userId < 1) {
            throw new BadSlotsRequestException("User ID has to be at least 1, but was: " + userId);
        }
    }

    private void validateBetAmount(BigDecimal betAmount) {
        if (betAmount == null) {
            throw new BadSlotsRequestException("Bet amount was null");
        }

        if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadSlotsRequestException("Bet amount has to be positive and grater than 0 but was: " + betAmount);
        }
    }

}
