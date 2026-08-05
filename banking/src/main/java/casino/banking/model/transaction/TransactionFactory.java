package casino.banking.model.transaction;

import casino.banking.util.GameService;

import java.math.BigDecimal;

public interface TransactionFactory {

    TransactionEntity createTransaction(GameService service, Long userId, BigDecimal amount);
}
