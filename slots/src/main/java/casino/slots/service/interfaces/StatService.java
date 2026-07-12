package casino.slots.service.interfaces;

import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;

public interface StatService {

    SlotsStatsDTO getStats();

    SlotsStatsUserDTO getStats(long userId);

}
