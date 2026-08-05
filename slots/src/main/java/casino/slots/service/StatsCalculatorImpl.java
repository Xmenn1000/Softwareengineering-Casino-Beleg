package casino.slots.service;

import casino.slots.model.SlotsGameEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class StatsCalculatorImpl implements SlotsStatsCalculator {

    @Override
    public long totalClientCount(List<SlotsGameEntity> games) {
        return games.stream()
                .map(SlotsGameEntity::getUserId)
                .distinct()
                .count();
    }

    @Override
    public long totalGamesCount(List<SlotsGameEntity> games) {
        return games.size();
    }

    @Override
    public BigDecimal totalProfit(List<SlotsGameEntity> games) {
        return sumOfAmounts(games).negate();
    }

    @Override
    public BigDecimal totalCashOut(List<SlotsGameEntity> games) {
        return sumOfPositiveAmounts(games);
    }

    @Override
    public BigDecimal totalTurnover(List<SlotsGameEntity> games) {
        return sumOfBets(games);
    }

    @Override
    public BigDecimal totalWinnings(List<SlotsGameEntity> games) {
        return sumOfPositiveAmounts(games);
    }

    @Override
    public BigDecimal totalLosses(List<SlotsGameEntity> games) {
        return games.stream()
                .map(SlotsGameEntity::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();
    }

    @Override
    public BigDecimal totalClientProfit(List<SlotsGameEntity> games) {
        return sumOfAmounts(games);
    }

    @Override
    public BigDecimal totalHouseTurnoverFromClient(List<SlotsGameEntity> games) {
        return sumOfBets(games);
    }

    @Override
    public BigDecimal totalHouseProfitFromClient(List<SlotsGameEntity> games) {
        return sumOfAmounts(games).negate();
    }

    private BigDecimal sumOfAmounts(List<SlotsGameEntity> games) {
        return games.stream()
                .map(SlotsGameEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumOfPositiveAmounts(List<SlotsGameEntity> games) {
        return games.stream()
                .map(SlotsGameEntity::getAmount)
                .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal sumOfBets(List<SlotsGameEntity> games) {
        return games.stream()
                .map(SlotsGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
