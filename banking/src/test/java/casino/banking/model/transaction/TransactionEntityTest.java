package casino.banking.model.transaction;

import casino.banking.exceptions.transaction.TransactionModelValidityBreachException;
import casino.banking.util.GameService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransactionEntityTest {

    // ---------- createTransaction ----------
    @Test
    void createTransaction_validArgs_createsEntity() {
        GameService service = GameService.SLOTS;
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("20");

        TransactionEntity result = assertDoesNotThrow(
                () -> TransactionEntity.createTransaction(service, userId, amount));

        assertEquals(service, result.getInvoicingParty());
        assertEquals(userId, result.getUserId());
        assertEquals(amount, result.getAmount());
        assertNull(result.getId());
    }

    @Test
    void createTransaction_negativeAmount_isAllowed() {
        BigDecimal loss = new BigDecimal("-20");

        TransactionEntity result = assertDoesNotThrow(
                () -> TransactionEntity.createTransaction(GameService.SLOTS, 1L, loss));

        assertEquals(loss, result.getAmount());
    }

    @Test
    void createTransaction_nullService_throws() {
        assertThrows(TransactionModelValidityBreachException.class,
                () -> TransactionEntity.createTransaction(null, 1L, new BigDecimal("20")));
    }

    @Test
    void createTransaction_nullUserId_throws() {
        assertThrows(TransactionModelValidityBreachException.class,
                () -> TransactionEntity.createTransaction(GameService.SLOTS, null, new BigDecimal("20")));
    }

    @Test
    void createTransaction_nullAmount_throws() {
        assertThrows(TransactionModelValidityBreachException.class,
                () -> TransactionEntity.createTransaction(GameService.SLOTS, 1L, null));
    }

    // ---------- replace ----------
    @Test
    void replace_validArgs_updatesFields() {
        TransactionEntity transaction = TransactionEntity.createTransaction(GameService.SLOTS, 1L, new BigDecimal("10"));

        transaction.replace(GameService.ROULETTE, 2L, new BigDecimal("-5"));

        assertEquals(GameService.ROULETTE, transaction.getInvoicingParty());
        assertEquals(2L, transaction.getUserId());
        assertEquals(new BigDecimal("-5"), transaction.getAmount());
    }

    @Test
    void replace_nullService_throws() {
        TransactionEntity transaction = TransactionEntity.createTransaction(GameService.SLOTS, 1L, new BigDecimal("10"));

        assertThrows(TransactionModelValidityBreachException.class,
                () -> transaction.replace(null, 1L, new BigDecimal("10")));
    }

    @Test
    void replace_nullUserId_throws() {
        TransactionEntity transaction = TransactionEntity.createTransaction(GameService.SLOTS, 1L, new BigDecimal("10"));

        assertThrows(TransactionModelValidityBreachException.class,
                () -> transaction.replace(GameService.SLOTS, null, new BigDecimal("10")));
    }

    @Test
    void replace_nullAmount_throws() {
        TransactionEntity transaction = TransactionEntity.createTransaction(GameService.SLOTS, 1L, new BigDecimal("10"));

        assertThrows(TransactionModelValidityBreachException.class,
                () -> transaction.replace(GameService.SLOTS, 1L, null));
    }
}
