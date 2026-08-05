package casino.banking.services.transction;

import casino.banking.exceptions.transaction.TransactionNotKnownException;
import casino.banking.model.transaction.TransactionEntity;
import casino.banking.model.transaction.TransactionFactory;
import casino.banking.repository.transaction.TransactionRepository;
import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.requestClients.transaction.UserRestClient;
import casino.banking.util.GameService;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRestClient userRestClient;

    @Mock
    private TransactionFactory transactionFactory;

    // ---------- createForUserId ----------
    @Test
    void createForUserId_validRequest_depositsSavesAndReturnsDTO() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("5.50");
        TransactionRequestDTO request = new TransactionRequestDTO(GameService.ROULETTE, amount);

        TransactionEntity entity = TransactionEntity.createTransaction(GameService.ROULETTE, userId, amount);
        when(transactionFactory.createTransaction(GameService.ROULETTE, userId, amount)).thenReturn(entity);

        UserTransactionDTO result = transactionService.createForUserId(userId, request);

        verify(userRestClient).depositBalanceById(userId, new BigInteger("5"), 50);
        verify(transactionRepository).save(entity);

        assertEquals(userId, result.getUserID());
        assertEquals(GameService.ROULETTE, result.getTransactionDTO().getGameService());
        assertEquals(0, amount.compareTo(result.getTransactionDTO().getAmount()));
    }

    // ---------- replaceById ----------
    @Test
    void replaceById_validRequest_updatesAndReturnsDTO() {
        Long transactionId = 10L;
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("-5");

        TransactionEntity entity = TransactionEntity.createTransaction(GameService.SLOTS, 99L, new BigDecimal("10"));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(entity));

        UserGameTransactionRequestDTO request = new UserGameTransactionRequestDTO(
                userId, new TransactionRequestDTO(GameService.ROULETTE, amount));

        UserTransactionDTO result = transactionService.replaceById(transactionId, request);

        assertEquals(userId, result.getUserID());
        assertEquals(GameService.ROULETTE, result.getTransactionDTO().getGameService());
        assertEquals(0, amount.compareTo(result.getTransactionDTO().getAmount()));
    }

    @Test
    void replaceById_idNotExists_throwsTransactionNotKnown() {
        Long transactionId = 10L;
        Long userId = 1L;
        UserGameTransactionRequestDTO request = new UserGameTransactionRequestDTO(
                userId, new TransactionRequestDTO(GameService.ROULETTE, new BigDecimal("-5")));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotKnownException.class,
                () -> transactionService.replaceById(transactionId, request));
    }

    // ---------- deleteById ----------
    @Test
    void deleteById_existingId_deletesAndReturnsDTO() {
        Long transactionId = 10L;
        Long userId = 1L;

        TransactionEntity entity = TransactionEntity.createTransaction(GameService.SLOTS, userId, new BigDecimal("3"));
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(entity));

        UserTransactionDTO result = transactionService.deleteById(transactionId);

        verify(transactionRepository).deleteById(transactionId);
        assertEquals(userId, result.getUserID());
        assertEquals(GameService.SLOTS, result.getTransactionDTO().getGameService());
    }

    @Test
    void deleteById_idNotExists_throwsTransactionNotKnown() {
        Long transactionId = 10L;
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotKnownException.class,
                () -> transactionService.deleteById(transactionId));
    }

    // ---------- findAll ----------
    @Test
    void findAll_oneTransaction_returnsInList() {
        Long userId = 1L;
        TransactionEntity entity = TransactionEntity.createTransaction(GameService.ROULETTE, userId, new BigDecimal("5"));
        when(transactionRepository.findAll()).thenReturn(List.of(entity));

        List<UserTransactionDTO> result = transactionService.findAll();

        assertEquals(1, result.size());
        assertEquals(userId, result.getFirst().getUserID());
        assertEquals(GameService.ROULETTE, result.getFirst().getTransactionDTO().getGameService());
    }

    @Test
    void findAll_noTransactions_returnsEmptyList() {
        when(transactionRepository.findAll()).thenReturn(List.of());

        assertTrue(transactionService.findAll().isEmpty());
    }

    // ---------- findByUserId ----------
    @Test
    void findByUserId_hasTransactions_returnsInList() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("3");
        TransactionEntity entity = TransactionEntity.createTransaction(GameService.SLOTS, userId, amount);
        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(entity));

        List<TransactionDTO> result = transactionService.findByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(GameService.SLOTS, result.getFirst().getGameService());
        assertEquals(0, amount.compareTo(result.getFirst().getAmount()));
    }

    @Test
    void findByUserId_noTransactions_returnsEmptyList() {
        Long userId = 1L;
        when(transactionRepository.findByUserId(userId)).thenReturn(List.of());

        assertTrue(transactionService.findByUserId(userId).isEmpty());
    }
}
