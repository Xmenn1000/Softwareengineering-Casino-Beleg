package casino.slots.validation;

import casino.slots.domain.enums.Symbol;
import casino.slots.exeptions.BadSlotsRequestException;
import casino.slots.model.SlotsGameEntity;

import java.math.BigDecimal;
import java.util.List;

public class SlotsGameEntityValidator {
    public void validate(SlotsGameEntity entity) {
        validateUser(entity.getUserId());
        validateAmount(entity.getAmount());
        validateBetAmount(entity.getBetAmount());
        validateSlotState(entity.getSlotStates());
    }

    private static void validateUser(Long user) {
        if (user == null) {
            throw new BadSlotsRequestException("User was empty");
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new BadSlotsRequestException("Amount was empty");
        }
    }

    private static void validateBetAmount(BigDecimal betAmount) {
        if (betAmount == null || betAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadSlotsRequestException("Bet amount has to be greater than 0");
        }
    }


    private static void validateSlotState(List<Symbol> symbols) {
        if (symbols.isEmpty()) {
            throw new BadSlotsRequestException("Slots is broken, there no symbols");
        }
    }
}
