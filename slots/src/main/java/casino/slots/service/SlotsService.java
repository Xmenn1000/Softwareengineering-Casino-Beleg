package casino.slots.service;

import casino.slots.view.SlotsGameDTO;
import casino.slots.view.SlotsGameResultDTO;
import casino.slots.view.SlotsStatsDTO;
import casino.slots.view.SlotsStatsUserDTO;

import java.util.List;

public interface SlotsService {

  //SlotsGameDTO play(slots game request);

  String getRules();

  String getChances();

  SlotsStatsDTO getStats();

  SlotsStatsUserDTO getStats(long userId);

  SlotsGameDTO getGame(long gameId);

  List<SlotsGameDTO> getAllGames();

  SlotsGameDTO deleteGame(long gameId);

}
