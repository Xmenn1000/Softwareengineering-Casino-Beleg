package casino.slots.service.interfaces;

import casino.slots.view.SlotsGameDTO;

import java.util.List;

public interface GameManagerService {
    SlotsGameDTO getGame(long gameId);

    List<SlotsGameDTO> getAllGames();

    SlotsGameDTO deleteGame(long gameId);
}
