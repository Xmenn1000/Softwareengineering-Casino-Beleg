package casino.roulette.service;

import casino.roulette.model.RouletteGameEntity;
import casino.roulette.view.RouletteStatsDTO;
import casino.roulette.view.RouletteUserStatsDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class RouletteStatsCalculator {

    public RouletteStatsDTO calculateStats(List<RouletteGameEntity> games) {
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

    public RouletteUserStatsDTO calculateUserStats(Long userId, List<RouletteGameEntity> games) {
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
}
