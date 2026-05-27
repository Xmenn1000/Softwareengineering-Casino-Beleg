package casino.banking.controller.transaction;

import casino.banking.util.GameService;
import casino.banking.view.transaction.request.TransactionRequestDTO;
import casino.banking.view.transaction.request.UserGameTransactionRequestDTO;
import casino.banking.view.transaction.request.UserTransactionRequestDTO;
import casino.banking.view.transaction.response.TransactionDTO;
import casino.banking.view.transaction.response.UserTransactionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class TransactionController implements TransactionApi {
    @Override
    public ResponseEntity<List<UserTransactionDTO>> getTransactions() {
        List<UserTransactionDTO> transactions;
        UserTransactionDTO trans1 = new UserTransactionDTO(1L, new TransactionDTO(1L, new BigDecimal(2)));
        UserTransactionDTO trans2 = new UserTransactionDTO(1L, new TransactionDTO(1L, new BigDecimal(2)));
        transactions = List.of(trans1, trans2);
        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> getTransactionByUserId(Long id) {
        List<TransactionDTO> transactions;
        TransactionDTO trans1 = new TransactionDTO(id, new BigDecimal(2000));
        TransactionDTO trans2 = new TransactionDTO(id, new BigDecimal(2000));
        transactions = List.of(trans1, trans2);
        return ResponseEntity.ok(transactions);
    }

    @Override
    public ResponseEntity<UserTransactionRequestDTO> createTransactionForUserId(Long userId, TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(
                new UserTransactionRequestDTO(
                        userId,
                        1L
                        , transactionRequestDTO
                ));
    }

    @Override
    public ResponseEntity<UserTransactionRequestDTO> createTransactionIdForTransaction(Long transactionId, UserGameTransactionRequestDTO userGameTransactionRequestDTO) {
        return ResponseEntity.ok(
                new UserTransactionRequestDTO(
                        userGameTransactionRequestDTO.getUserId(),
                        transactionId,
                        new TransactionRequestDTO(
                                GameService.SLOTS,
                                BigDecimal.valueOf(2000)
                        )
                ));
    }

    @Override
    public ResponseEntity<UserGameTransactionRequestDTO> deleteTransactionByTransactionId(Long transactionId, UserTransactionRequestDTO userGameTransactionRequestDTO) {
        return ResponseEntity.ok(
                new UserGameTransactionRequestDTO(
                        userGameTransactionRequestDTO.getUserId(),
                        new TransactionRequestDTO(
                                GameService.SLOTS,
                                BigDecimal.valueOf(2000)
                        )
                ));
    }

}