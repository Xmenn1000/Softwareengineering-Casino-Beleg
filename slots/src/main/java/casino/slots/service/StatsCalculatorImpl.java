package casino.slots.service;

import casino.slots.model.SlotsGameEntity;

import java.math.BigDecimal;
import java.util.List;

public class StatsCalculatorImpl implements SlotsStatsCalculator{
    @Override
    public long totalClientCount(List<SlotsGameEntity> games) {
        return games.stream().map(SlotsGameEntity::getUserId).distinct().count();
    }

    @Override
    public long totalGamesCount(List<SlotsGameEntity> games) {
        return games.size();
    }

    @Override
    public BigDecimal totalProfit(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalCashOut(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalTurnover(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalWinnings(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalLosses(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalClientProfit(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalHouseTurnoverFromClient(List<SlotsGameEntity> games) {
        return null;
    }

    @Override
    public BigDecimal totalHouseProfitFromClient(List<SlotsGameEntity> games) {
        return null;
    }
}
