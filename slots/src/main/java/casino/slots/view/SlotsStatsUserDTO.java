package casino.slots.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SlotsStatsUserDTO {

  private long client;
  private long totalGamesCount;
  private BigDecimal totalWinnings;
  private BigDecimal totalLosses;
  private BigDecimal totalClientProfit;
  private BigDecimal totalHouseTurnoverFromClient;
  private BigDecimal totalHouseProfitFromClient;

}
