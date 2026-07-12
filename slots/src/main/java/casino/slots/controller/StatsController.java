package casino.slots.controller;

import casino.slots.service.StatsService;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController implements StatsControllerAPI {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @Override
    public ResponseEntity<SlotsStatsDTO> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }

    @Override
    public ResponseEntity<SlotsStatsUserDTO> getStatsByUserId(Long userId) {
        return ResponseEntity.ok(statsService.getStatsByUserId(userId));
    }
}
