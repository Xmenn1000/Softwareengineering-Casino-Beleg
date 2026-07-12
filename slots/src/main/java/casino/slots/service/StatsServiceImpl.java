package casino.slots.service;

import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {

    //TODO: implement by aggregating over the persisted game rounds

    @Override
    public SlotsStatsDTO getStats() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public SlotsStatsUserDTO getStatsByUserId(Long userId) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
