package casino.slots.view;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class SlotsGameResultDTO {

  private long id;
  private long user;
  private boolean winning;
  private BigDecimal amount;
  // var slot_states --> landing symbol of the slots missing

}
