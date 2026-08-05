package casino.banking.mapper.transaction;

import casino.banking.model.transaction.TransactionEntity;
import casino.banking.util.GameService;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionMapperTest {

    private TransactionEntity dummyTransaction;

    @BeforeEach
    void setUp() {
        Long id = 1L;
        GameService invoicingParty = GameService.ROULETTE;
        Long userId = 42L;
        BigDecimal amount = new BigDecimal(20);

        dummyTransaction = mock(TransactionEntity.class);
        when(dummyTransaction.getId()).thenReturn(id);
        when(dummyTransaction.getInvoicingParty()).thenReturn(invoicingParty);
        when(dummyTransaction.getUserId()).thenReturn(userId);
        when(dummyTransaction.getAmount()).thenReturn(amount);
    }

    @Test
    void toDto_valid_createsDTOWithFields() {
        TransactionDTO newTransactionDto = TransactionMapper.toDto(dummyTransaction);
        assertEquals(newTransactionDto.getId(), dummyTransaction.getId());
        assertEquals(newTransactionDto.getGameService(), dummyTransaction.getInvoicingParty());
        assertEquals(newTransactionDto.getAmount(), dummyTransaction.getAmount());
    }

    @Test
    void toUserTransactionDto_valid_createsDTOWithFields() {
        UserTransactionDTO newUserTransactionDto = TransactionMapper.toUserTransactionDto(dummyTransaction);
        assertEquals(newUserTransactionDto.getUserID(), dummyTransaction.getUserId());
        assertEquals(newUserTransactionDto.getTransactionDTO().getId(), dummyTransaction.getId());
        assertEquals(newUserTransactionDto.getTransactionDTO().getGameService(), dummyTransaction.getInvoicingParty());
        assertEquals(newUserTransactionDto.getTransactionDTO().getAmount(), dummyTransaction.getAmount());
    }
}
