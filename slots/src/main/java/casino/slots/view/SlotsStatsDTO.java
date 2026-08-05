package casino.slots.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SlotsStatsDTO {

  private long totalClientCount;
  private long totalGamesCount;
  private BigDecimal totalProfit;
  private BigDecimal totalCashOut;
  private BigDecimal totalTurnover;

}
