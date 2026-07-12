package casino.slots.service;

import casino.slots.view.SlotsGameDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameHistoryServiceImpl implements GameHistoryService {

    //TODO: implement using SlotsGameRepository

    @Override
    public List<SlotsGameDTO> findAll() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public SlotsGameDTO findById(Long gameId) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public SlotsGameDTO deleteById(Long gameId) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
