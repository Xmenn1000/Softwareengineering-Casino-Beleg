package casino.slots.domain.dto;

import casino.slots.domain.enums.Symbol;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class GameResult {

    private boolean winning;
    private BigDecimal amount;
    private List<Symbol> SlotStates;
}
