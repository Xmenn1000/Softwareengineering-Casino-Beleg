package casino.roulette.service;

import casino.roulette.requestClients.banking.BankingRestClient;
import casino.roulette.game.RouletteEngine;
import casino.roulette.request.RoulettePlayRequestDTO;
import casino.roulette.view.RouletteGameDTO;
import casino.roulette.view.RoulettePlayResultDTO;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import casino.roulette.exceptions.RouletteGameNotFoundException;
import casino.roulette.mapper.RouletteGameMapper;
import casino.roulette.model.RouletteGameEntity;
import casino.roulette.repository.RouletteGameRepository;
import casino.roulette.validation.RouletteRequestValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouletteServiceImpl implements RouletteService {

    private final RouletteGameRepository rouletteGameRepository;
    private final RouletteEngine rouletteEngine;
    private final BankingRestClient bankingRestClient;
    private final RouletteStatsCalculator rouletteStatsCalculator;
    private final RouletteInfoService rouletteInfoService;
    private final RouletteRequestValidator rouletteRequestValidator;

    public RouletteServiceImpl(
            RouletteGameRepository rouletteGameRepository,
            RouletteEngine rouletteEngine,
            BankingRestClient bankingRestClient,
            RouletteStatsCalculator rouletteStatsCalculator,
            RouletteInfoService rouletteInfoService,
            RouletteRequestValidator rouletteRequestValidator
    ) {
        this.rouletteGameRepository = rouletteGameRepository;
        this.rouletteEngine = rouletteEngine;
        this.bankingRestClient = bankingRestClient;
        this.rouletteStatsCalculator = rouletteStatsCalculator;
        this.rouletteInfoService = rouletteInfoService;
        this.rouletteRequestValidator = rouletteRequestValidator;
    }

    @Override
    public RoulettePlayResultDTO play(RoulettePlayRequestDTO request) {
        rouletteRequestValidator.validatePlayRequest(request);

        bankingRestClient.findUserById(request.getUser());

        RouletteGameEntity game = rouletteEngine.play(request);

        bankingRestClient.createRouletteTransaction(game.getUser(), game.getAmount());

        RouletteGameEntity savedGame = rouletteGameRepository.save(game);

        return RouletteGameMapper.toPlayResultDto(savedGame);
    }

    @Override
    public String getRules() {
        return rouletteInfoService.getRules();
    }

    @Override
    public String getChances() {
        return rouletteInfoService.getChances();
    }

    @Override
    public RouletteStatsDTO getStats() {
        return rouletteStatsCalculator.calculateStats(rouletteGameRepository.findAll());
    }

    @Override
    public RouletteUserStatsDTO getUserStats(Long userId) {
        rouletteRequestValidator.validateUserId(userId);
        bankingRestClient.findUserById(userId);

        return rouletteStatsCalculator.calculateUserStats(userId, rouletteGameRepository.findByUser(userId));
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
