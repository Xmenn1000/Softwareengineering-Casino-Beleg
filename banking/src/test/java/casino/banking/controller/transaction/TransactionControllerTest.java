package casino.banking.controller.transaction;

import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.services.transction.TransactionService;
import casino.banking.util.GameService;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    // ---------- findAll ----------
    @Test
    void findAll_returnsListBody() {
        UserTransactionDTO transactionDummy1 = new UserTransactionDTO(1L, new TransactionDTO(10L, GameService.ROULETTE, new BigDecimal("5")));
        UserTransactionDTO transactionDummy2 = new UserTransactionDTO(2L, new TransactionDTO(11L, GameService.SLOTS, new BigDecimal("-3")));
        List<UserTransactionDTO> transactions = List.of(transactionDummy1, transactionDummy2);
        when(transactionService.findAll()).thenReturn(transactions);

        ResponseEntity<List<UserTransactionDTO>> response = transactionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).findAll();
    }

    // ---------- findByUserId ----------
    @Test
    void findByUserId_returnsListBody() {
        Long userId = 1L;
        TransactionDTO transactionDummy1 = new TransactionDTO(10L, GameService.ROULETTE, new BigDecimal("5"));
        TransactionDTO transactionDummy2 = new TransactionDTO(11L, GameService.SLOTS, new BigDecimal("-3"));
        List<TransactionDTO> transactions = List.of(transactionDummy1, transactionDummy2);
        when(transactionService.findByUserId(userId)).thenReturn(transactions);

        ResponseEntity<List<TransactionDTO>> response = transactionController.findByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
        verify(transactionService, times(1)).findByUserId(userId);
    }

    // ---------- createForUserId ----------
    @Test
    void createForUserId_returnsCreatedTransactionBody() {
        Long userId = 1L;
        TransactionRequestDTO request = new TransactionRequestDTO(GameService.ROULETTE, new BigDecimal("5"));
        UserTransactionDTO dto = new UserTransactionDTO(userId, new TransactionDTO(10L, GameService.ROULETTE, new BigDecimal("5")));
        when(transactionService.createForUserId(userId, request)).thenReturn(dto);

        ResponseEntity<UserTransactionDTO> response = transactionController.createForUserId(userId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(transactionService, times(1)).createForUserId(userId, request);
    }

    // ---------- replaceById ----------
    @Test
    void replaceById_returnsUpdatedTransactionBody() {
        Long transactionId = 10L;
        UserGameTransactionRequestDTO request = new UserGameTransactionRequestDTO(
                1L, new TransactionRequestDTO(GameService.SLOTS, new BigDecimal("7")));
        UserTransactionDTO dto = new UserTransactionDTO(1L, new TransactionDTO(transactionId, GameService.SLOTS, new BigDecimal("7")));
        when(transactionService.replaceById(transactionId, request)).thenReturn(dto);

        ResponseEntity<UserTransactionDTO> response = transactionController.replaceById(transactionId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(transactionService, times(1)).replaceById(transactionId, request);
    }

    // ---------- deleteById ----------
    @Test
    void deleteById_returnsDeletedTransactionBody() {
        Long transactionId = 10L;
        UserTransactionDTO dto = new UserTransactionDTO(1L, new TransactionDTO(transactionId, GameService.ROULETTE, new BigDecimal("5")));
        when(transactionService.deleteById(transactionId)).thenReturn(dto);

        ResponseEntity<UserTransactionDTO> response = transactionController.deleteById(transactionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
        verify(transactionService, times(1)).deleteById(transactionId);
    }
}
