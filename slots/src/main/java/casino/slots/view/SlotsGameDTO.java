package casino.slots.view;

import casino.slots.domain.enums.Symbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class SlotsGameDTO {

  private long id;
  private long user;
  private boolean winning;
  private BigDecimal amount;
  private BigDecimal betAmount;
  private List<Symbol> slotStates;
}
