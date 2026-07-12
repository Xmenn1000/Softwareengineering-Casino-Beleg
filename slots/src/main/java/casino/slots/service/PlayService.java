package casino.slots.service;

import casino.slots.request.SlotsPlayRequest;
import casino.slots.view.SlotsGameResultDTO;

public interface PlayService {

    SlotsGameResultDTO play(SlotsPlayRequest playRequest);
}
