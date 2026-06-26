package casino.roulette.service;

import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.util.BetType;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import casino.roulette.exceptions.RouletteGameNotFoundException;
import casino.roulette.mapper.RouletteGameMapper;
import casino.roulette.model.RouletteGameEntity;
import casino.roulette.repository.RouletteGameRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RouletteServiceImpl implements RouletteService {

    private final RouletteGameRepository rouletteGameRepository;

    public RouletteServiceImpl(RouletteGameRepository rouletteGameRepository) {
        this.rouletteGameRepository = rouletteGameRepository;
    }

    @Override
    public RoulettePlayResultDTO play(RoulettePlayRequestDTO request) {
        RouletteGameEntity game = RouletteGameEntity.create(
                request.getUser(),
                false,
                request.getAmount().negate(),
                request.getAmount(),
                request.getBetType(),
                request.getBetValue(),
                0
        );

        RouletteGameEntity savedGame = rouletteGameRepository.save(game);

        return RouletteGameMapper.toPlayResultDto(savedGame);
    }

    @Override
    public String getRules() {
        return "Roulette rules will be provided here.";
    }

    @Override
    public String getChances() {
        return "Roulette chances and payouts will be provided here.";
    }

    @Override
    public RouletteStatsDTO getStats() {
        return new RouletteStatsDTO(
                0,
                0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    @Override
    public RouletteUserStatsDTO getUserStats(Long userId) {
        return new RouletteUserStatsDTO(
                userId,
                0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    @Override
    public List<RouletteGameDTO> getGames() {
        return rouletteGameRepository.findAll()     // gets all entity entries from db
                .stream()
                .map(RouletteGameMapper::toGameDto) // + stream(): entity -> DTO
                .toList();                          // makes list out of it again
    }

    @Override
    public RouletteGameDTO getGame(Long gameId) {
        RouletteGameEntity game = rouletteGameRepository.findById(gameId) // findById returns 'optional', no object, because could be no id
                .orElseThrow(() -> new RouletteGameNotFoundException(gameId));

        return RouletteGameMapper.toGameDto(game);
    }

    @Override
    public RouletteGameDTO deleteGame(Long gameId) {
        RouletteGameEntity game = rouletteGameRepository.findById(gameId)
                .orElseThrow(() -> new RouletteGameNotFoundException(gameId));

        rouletteGameRepository.delete(game);

        return RouletteGameMapper.toGameDto(game);
    }
}
