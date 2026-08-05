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
  private long total_games_count;
  private BigDecimal total_winnings;
  private BigDecimal total_losses;
  private BigDecimal total_client_profit;
  private BigDecimal total_house_turnover_from_client;
  private BigDecimal total_house_profit_from_client;

}
