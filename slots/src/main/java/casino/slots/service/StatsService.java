package casino.slots.service;

import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;

public interface StatsService {

    SlotsStatsDTO getStats();

    SlotsStatsUserDTO getStatsByUserId(Long userId);
}
