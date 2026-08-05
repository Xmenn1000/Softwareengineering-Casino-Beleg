package casino.slots.service;

import casino.slots.view.SlotsGameDTO;

import java.util.List;

public interface GameHistoryService {

    List<SlotsGameDTO> findAll();
    SlotsGameDTO findById(Long gameId);
    SlotsGameDTO deleteById(Long gameId);
}
