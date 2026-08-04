package casino.slots.model;

import casino.slots.domain.dto.OutCome;
import casino.slots.domain.enums.Symbol;
import casino.slots.validation.SlotsGameEntityValidator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "SlotsGames")
@Entity
public class  SlotsGameEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id", nullable = false, updatable = false)
  private long id;

  @Column(name = "UserId", nullable = false)
  private long userId;

  @Column(name = "Winning", nullable = false)
  private boolean winning;

  @Column(name = "Amount", nullable = false)
  private BigDecimal amount;

  @Column(name = "BetAmount", nullable = false)
  private BigDecimal betAmount;

  @Column(name = "SlotStates", nullable = false)
  private List<Symbol> slotStates;

  public static SlotsGameEntity create(
          Long user,
          boolean winning,
          BigDecimal amount,
          BigDecimal betAmount,
          List<Symbol> slotStates
  ) {
    SlotsGameEntityValidator validator = new SlotsGameEntityValidator();

    SlotsGameEntity entity = SlotsGameEntityFactory.create(user, winning, amount, betAmount, slotStates);

    validator.validate(entity);

    return entity;
  }

}
