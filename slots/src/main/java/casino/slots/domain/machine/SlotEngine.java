package casino.slots.domain.machine;

import casino.slots.model.SlotsGameEntity;
import casino.slots.request.SlotsPlayRequest;


public interface SlotEngine {

    SlotsGameEntity play(SlotsPlayRequest request);
}
