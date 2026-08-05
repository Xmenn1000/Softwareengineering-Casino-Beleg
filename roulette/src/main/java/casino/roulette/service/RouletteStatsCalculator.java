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
        long totalClientCount = games.stream()
                .map(RouletteGameEntity::getUser)
                .distinct()
                .count();

        long totalGamesCount = games.size();

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
