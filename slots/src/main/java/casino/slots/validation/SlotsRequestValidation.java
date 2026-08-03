package casino.slots.validation;

import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class SlotsRequestValidation {

    public void validatePlayRequest(SlotsGameDTO gameDTO) {
        if (gameDTO == null) {
            throw new BadSlotsRequestException("RequestBody was null");
        }

        validateUserId(gameDTO.getUser());
        validateSymbols(gameDTO.getSymbols());
        validateBetAmount(gameDTO.getBetAmount());
        validateResultingAmount(gameDTO.getResultingAmount());

    }

    public void validateUserId(Long userId) {
        if (userId == null) {
            throw new BadSlotsRequestException("User Id was null");
        }

        if (userId < 0) {
            throw new BadSlotsRequestException("User ID has to be at least 0, but was: " + userId);
        }
    }

    public void validateResultingAmount(BigDecimal resultingAmount) {
        if (resultingAmount == null) {
            throw new BadSlotsRequestException("There is no outcome amount of the game");
        }
    }

    public void validateBetAmount(BigDecimal betAmount) {
        if (betAmount == null) {
            throw new BadSlotsRequestException("Bet amount was null");
        }

        if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadSlotsRequestException("Bet amount has to be positive and grater than 0 but was: " + betAmount);
        }
    }

    public void validateSymbols(List<Symbol> symbols) {
        if (symbols == null) {
            throw new BadSlotsRequestException("SlotsSymbols are null");
        }
    }
}
