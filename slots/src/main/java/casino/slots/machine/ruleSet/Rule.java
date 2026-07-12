package casino.slots.machine.ruleSet;

import casino.slots.machine.OutCome;

import java.math.BigDecimal;

public interface Rule {
    BigDecimal payOut(OutCome result, BigDecimal amount);
}
