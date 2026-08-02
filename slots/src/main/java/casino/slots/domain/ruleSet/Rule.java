package casino.slots.domain.ruleSet;

import casino.slots.domain.dto.OutCome;

import java.math.BigDecimal;

public interface Rule {
    BigDecimal payOut(OutCome result, BigDecimal amount);
}
