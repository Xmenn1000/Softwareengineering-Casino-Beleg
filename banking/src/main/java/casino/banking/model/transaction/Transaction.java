package casino.banking.model.transaction;

import casino.banking.util.GameService;

import java.math.BigDecimal;

public interface Transaction {
    Long getId();
    GameService getInvoicingParty();
    Long getUserId();
    BigDecimal getAmount();
}
