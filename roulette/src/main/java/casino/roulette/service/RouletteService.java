package casino.roulette.service;


import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;

import java.util.List;

public interface RouletteService {
    RoulettePlayResultDTO play(RoulettePlayRequestDTO request);

    String getRules();

    String getChances();

    RouletteStatsDTO getStats();

    RouletteUserStatsDTO getUserStats(Long userId);

    List<RouletteGameDTO> getGames();

    RouletteGameDTO getGame(Long gameId);

    RouletteGameDTO deleteGame(Long gameId);
}
