package casino.slots.domain.machine;

import casino.slots.domain.dto.GameResult;
import casino.slots.model.SlotsGameEntity;
import casino.slots.request.SlotsPlayRequest;

import java.math.BigDecimal;


public interface SlotEngine {

    GameResult play(BigDecimal betAmount);
}
