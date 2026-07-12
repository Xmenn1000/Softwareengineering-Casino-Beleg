package casino.slots.service.interfaces;

import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameResultDTO;

public interface SlotsGameService {

  SlotsGameResultDTO play(SlotsPlayRequest playRequest);

}
