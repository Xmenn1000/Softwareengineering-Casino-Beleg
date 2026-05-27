package casino.banking.controller.transaction;

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
        return null;
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> createTransactionForUserId(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> getUser(Long id) {
        return null;
    }
}
