package casino.banking.domain;

import java.math.BigDecimal;

public interface BalanceHolder {
    BigDecimal getBalanceAmount();
    BigDecimal setBalanceAmount(BigDecimal amount);
}
