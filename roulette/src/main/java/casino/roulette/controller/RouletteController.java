package casino.roulette.controller;

import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.service.RouletteService;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RouletteController implements RouletteApi {

    private final RouletteService rouletteService;

    public RouletteController(RouletteService rouletteService) {
        this.rouletteService = rouletteService;
    }

    @Override
    public ResponseEntity<RoulettePlayResultDTO> play(RoulettePlayRequestDTO request) {
        return ResponseEntity.ok(rouletteService.play(request));
    }

    @Override
    public ResponseEntity<String> getRules() {
        return ResponseEntity.ok(rouletteService.getRules());
    }

    @Override
    public ResponseEntity<String> getChances() {
        return ResponseEntity.ok(rouletteService.getChances());
    }

    @Override
    public ResponseEntity<RouletteStatsDTO> getStats() {
        return ResponseEntity.ok(rouletteService.getStats());
    }

    @Override
    public ResponseEntity<RouletteUserStatsDTO> getUserStats(Long userId) {
        return ResponseEntity.ok(rouletteService.getUserStats(userId));
    }

    @Override
    public ResponseEntity<List<RouletteGameDTO>> getGames() {
        return ResponseEntity.ok(rouletteService.getGames());
    }

    @Override
    public ResponseEntity<RouletteGameDTO> getGame(Long gameId) {
        return ResponseEntity.ok(rouletteService.getGame(gameId));
    }

    @Override
    public ResponseEntity<RouletteGameDTO> deleteGame(Long gameId) {
        return ResponseEntity.ok(rouletteService.deleteGame(gameId));
    }
}
