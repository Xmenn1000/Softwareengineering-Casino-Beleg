package casino.slots.view;

import casino.slots.domain.enums.Symbol;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class SlotsGameDTO {

  private long user;
  private long id;
  private boolean winning;
  private BigDecimal resultingAmount;
  private BigDecimal betAmount;
  private List<Symbol> symbols;

}
