package casino.slots.model;

import casino.slots.domain.enums.Symbol;

import java.math.BigDecimal;
import java.util.List;

public class SlotsGameEntityFactory {
    public static SlotsGameEntity create(Long userId, boolean winning, BigDecimal amount, BigDecimal betAmount, List<Symbol> slotStates) {
        SlotsGameEntity entity = new SlotsGameEntity();
        entity.setUserId(userId);
        entity.setWinning(winning);
        entity.setAmount(amount);
        entity.setBetAmount(betAmount);
        entity.setSlotStates(slotStates);

        return entity;
    }
}
