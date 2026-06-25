package casino.roulette.controller;

import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/casino/roulette/api")
public interface RouletteApi {

    @PostMapping("/play")
    ResponseEntity<RoulettePlayResultDTO> play(
            @RequestBody RoulettePlayRequestDTO request
    );

    @GetMapping("/info/rules")
    ResponseEntity<String> getRules();

    @GetMapping("/info/chances")
    ResponseEntity<String> getChances();

    @GetMapping("/stats")
    ResponseEntity<RouletteStatsDTO> getStats();

    @GetMapping("/stats/user/{userId}")
    ResponseEntity<RouletteUserStatsDTO> getUserStats(
            @PathVariable Long userId
    );

    @GetMapping("/stats/games")
    ResponseEntity<List<RouletteGameDTO>> getGames();

    @GetMapping("/stat/{gameId}")
    ResponseEntity<RouletteGameDTO> getGame(
            @PathVariable Long gameId
    );

    @DeleteMapping("/stat/{gameId}")
    ResponseEntity<RouletteGameDTO> deleteGame(
            @PathVariable Long gameId
    );
}
