package casino.banking.model.user;

import java.math.BigDecimal;

public interface User {
    Long getId();
    String getFirstName();
    String getLastName();
    BigDecimal getBalance();
}
