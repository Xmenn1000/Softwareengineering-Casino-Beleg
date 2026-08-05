package casino.banking.model.transaction;

import casino.banking.util.GameService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class TransactionFactoryImplTest {

    @Test
    void createTransaction_validTransaction_createsTransaction() {
        GameService service = GameService.SLOTS;
        Long userId = 1L;
        BigDecimal amount = new BigDecimal(20);


        TransactionFactory factory = new TransactionFactoryImpl();
        Transaction createdTransaction = factory.createTransaction(service, userId, amount);
        assertEquals(service, createdTransaction.getInvoicingParty());
        assertEquals(userId, createdTransaction.getUserId());
        assertEquals(createdTransaction.getAmount(), amount);
        assertNull(createdTransaction.getId());
    }
}
