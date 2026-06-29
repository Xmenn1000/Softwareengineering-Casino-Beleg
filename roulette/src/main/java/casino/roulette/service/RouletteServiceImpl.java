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
        return """
            European roulette uses numbers from 0 to 36. A player places a bet on a supported betting option and the ball lands on one number.
            The supported betting options are:
            STRAIGHT_NUMBER: bet on one number from 0 to 36.
            COLOR: bet on RED or BLACK. Number 0 has no color and loses color bets.
            PARITY: bet on EVEN or ODD. Number 0 is neither even nor odd and loses parity bets.
            RANGE: bet on LOW (1-18) or HIGH (19-36). Number 0 loses range bets.
            DOZEN: bet on FIRST (1-12), SECOND (13-24), or THIRD (25-36). Number 0 loses dozen bets.
            Each API call represents exactly one completed game round.
            """;
    }

    @Override
    public String getChances() {
        return """
            European roulette has 37 possible ball positions: 0-36.
            STRAIGHT_NUMBER: win chance 1/37, payout 35:1.
            COLOR: win chance 18/37, payout 1:1.
            PARITY: win chance 18/37, payout 1:1.
            RANGE: win chance 18/37, payout 1:1.
            DOZEN: win chance 12/37, payout 2:1.
            Profit formula:
            Winning round: amount = betAmount * payoutMultiplier.
            Losing round: amount = -betAmount.
            House edge is caused by number 0.
            """;
    }

    @Override
    public RouletteStatsDTO getStats() {
        List<RouletteGameEntity> games = rouletteGameRepository.findAll();

        // ".stream()": goes through all elements in games (game 1 -> game 2 -> game 3...)
        // ".map(RouletteGameEntity::getUser)": gets from every game only the user-id (e.g. [1, 1, 2, 5])
        //   --> "::" = method-reference; same as "game -> game.getUser()"
        // ".distinct()": deletes doubled values (e.g. [1, 1, 2, 5] -> [1, 2, 5])
        // ".count()": counts elements (e.g. [1, 2, 5] -> 3
        long totalClientCount = games.stream()
                .map(RouletteGameEntity::getUser)
                .distinct()
                .count();

        long totalGamesCount = games.size();

        // ".filter()": only values, where condition is true
        // "amount -> amount.compareTo(BigDecimal.ZERO) > 0": take value with name "amount" and check if amount > 0
        //   --> BigDecimal is an object, objects cannot be compared with ">" --> .compareTo() (returns
        //   negative/positive number or 0) (e.g. [-10, 20, -5, 35] --> [20, 35])
        // ".reduce(BigDecimal.ZERO, BigDecimal::add)": reduce() = sum up all values, here: "add up everything, start
        //   on 0" (e.g. [20, 35] --> 0 + 20 + 35 = 55)
        BigDecimal totalCashOut = games.stream()
                .map(RouletteGameEntity::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTurnover = games.stream()
                .map(RouletteGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalClientProfit = games.stream()
                .map(RouletteGameEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ".negate": turns over signs (e.g. 40 --> -40)
        BigDecimal totalProfit = totalClientProfit.negate();

        return new RouletteStatsDTO(
                totalClientCount,
                totalGamesCount,
                totalProfit,
                totalCashOut,
                totalTurnover
        );
    }

    @Override
    public RouletteUserStatsDTO getUserStats(Long userId) {
        List<RouletteGameEntity> games = rouletteGameRepository.findByUser(userId);

        long totalGamesCount = games.size();

        BigDecimal totalWinnings = games.stream()
                .map(RouletteGameEntity::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ".abs()": makes negative number positive (because losses are often displayed positive in statistics)
        BigDecimal totalLosses = games.stream()
                .map(RouletteGameEntity::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();

        BigDecimal totalClientProfit = games.stream()
                .map(RouletteGameEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalHouseTurnoverFromClient = games.stream()
                .map(RouletteGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalHouseProfitFromClient = totalClientProfit.negate();

        return new RouletteUserStatsDTO(
                userId,
                totalGamesCount,
                totalWinnings,
                totalLosses,
                totalClientProfit,
                totalHouseTurnoverFromClient,
                totalHouseProfitFromClient
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
