package casino.slots.model;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "slots_game")

public class SlotsGameEntity {

  private long id;
  private long userId;
  private boolean winning;
  private BigDecimal amount;
  //private slotState slotStates;

}
