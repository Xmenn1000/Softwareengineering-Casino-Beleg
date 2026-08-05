package casino.slots.service;

import casino.slots.model.SlotsGameEntity;

import java.math.BigDecimal;
import java.util.List;

public interface SlotsStatsCalculator {

    long totalClientCount(List<SlotsGameEntity> games);
    long totalGamesCount(List<SlotsGameEntity> games);
    BigDecimal totalProfit(List<SlotsGameEntity> games);
    BigDecimal totalCashOut(List<SlotsGameEntity> games);
    BigDecimal totalTurnover(List<SlotsGameEntity> games);
    BigDecimal totalWinnings(List<SlotsGameEntity> games);
    BigDecimal totalLosses(List<SlotsGameEntity> games);
    BigDecimal totalClientProfit(List<SlotsGameEntity> games);
    BigDecimal totalHouseTurnoverFromClient(List<SlotsGameEntity> games);
    BigDecimal totalHouseProfitFromClient(List<SlotsGameEntity> games);
}
