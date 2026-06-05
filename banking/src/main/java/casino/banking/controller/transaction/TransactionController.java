package casino.banking.controller.transaction;

import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.request.transaction.UserTransactionRequestDTO;
import casino.banking.services.transction.TransactionService;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public ResponseEntity<List<UserTransactionDTO>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @Override
    public ResponseEntity<List<TransactionDTO>> findByUserId(Long id) {
        return ResponseEntity.ok(transactionService.findByUserId(id));
    }

    @Override
    public ResponseEntity<UserTransactionDTO> createForUserId(Long userId, TransactionRequestDTO transactionRequestDTO) {
        return ResponseEntity.ok(transactionService.createForUserId(userId, transactionRequestDTO));
    }

    @Override
    public ResponseEntity<UserTransactionDTO> replaceById(Long transactionId, UserGameTransactionRequestDTO userGameTransactionRequestDTO) {
        return ResponseEntity.ok(transactionService.replaceById(transactionId, userGameTransactionRequestDTO));
    }

    @Override
    public ResponseEntity<UserTransactionDTO> deleteById(Long transactionId, UserTransactionRequestDTO userGameTransactionRequestDTO) {
        return ResponseEntity.ok(transactionService.deleteById(transactionId, userGameTransactionRequestDTO));
    }

}
