package casino.slots.view;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SlotsStatsDTO {

  private long total_client_count;
  private long total_games_count;
  private BigDecimal total_profit;
  private BigDecimal total_cashout;
  private BigDecimal total_turnover;

}
