package casino.banking.model.transaction;

import casino.banking.util.GameService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionFactoryImpl implements TransactionFactory {

    @Override
    public TransactionEntity createTransaction(GameService service, Long userId, BigDecimal amount) {
        return TransactionEntity.createTransaction(service, userId, amount);
    }
}
