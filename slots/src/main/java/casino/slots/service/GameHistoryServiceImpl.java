package casino.slots.service;

import casino.slots.exeptions.SlotsGameNotFoundException;
import casino.slots.mapper.SlotsGameMapper;
import casino.slots.model.SlotsGameEntity;
import casino.slots.repository.SlotsGameRepository;
import casino.slots.view.SlotsGameDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameHistoryServiceImpl implements GameHistoryService {

    private final SlotsGameRepository repository;

    public GameHistoryServiceImpl(SlotsGameRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SlotsGameDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(SlotsGameMapper::toDTO)
                .toList();
    }

    @Override
    public SlotsGameDTO findById(Long gameId) {
        SlotsGameEntity game = repository.findById(gameId)
                .orElseThrow(() ->
                        new SlotsGameNotFoundException(gameId)
                );

        return SlotsGameMapper.toDTO(game);
    }

    @Override
    public SlotsGameDTO deleteById(Long gameId) {
        SlotsGameEntity game = repository.findById(gameId)
                .orElseThrow(() ->
                        new SlotsGameNotFoundException(gameId)
                );

        repository.delete(game);

        return SlotsGameMapper.toDTO(game);
    }
}