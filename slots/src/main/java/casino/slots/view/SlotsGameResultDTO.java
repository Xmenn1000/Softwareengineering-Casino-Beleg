package casino.slots.view;

import casino.slots.domain.enums.Symbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class SlotsGameResultDTO {

  private long userId;
  private boolean winning;
  private BigDecimal amount;
  private List<Symbol> slotStates;
  private BigDecimal betAmount;

}