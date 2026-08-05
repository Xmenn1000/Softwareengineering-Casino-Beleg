package casino.slots.service;

import casino.slots.repository.SlotsGameRepository;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.springframework.stereotype.Service;

@Service
public class StatsServiceImpl implements StatsService {

    private final SlotsGameRepository slotsGameRepository;

    public StatsServiceImpl(SlotsGameRepository slotsGameRepository) {
        this.slotsGameRepository = slotsGameRepository;
    }

    @Override
    public SlotsStatsDTO getStats() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public SlotsStatsUserDTO getStatsByUserId(Long userId) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
