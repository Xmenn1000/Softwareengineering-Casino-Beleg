package casino.roulette.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RouletteUserStatsDTO {
    private Long client;
    private long totalGamesCount;
    private BigDecimal totalWinnings;
    private BigDecimal totalLosses;
    private BigDecimal totalClientProfit;
    private BigDecimal totalHouseTurnoverFromClient;
    private BigDecimal totalHouseProfitFromClient;
}
