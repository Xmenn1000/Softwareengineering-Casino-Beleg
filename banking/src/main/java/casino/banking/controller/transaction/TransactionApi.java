package casino.banking.controller.transaction;

import casino.banking.util.GameService;
import casino.banking.view.transaction.request.TransactionRequestDTO;
import casino.banking.view.transaction.response.TransactionDTO;
import casino.banking.view.transaction.response.UserTransactionDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.hibernate.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/casino/bank/api")
@RestController
public interface TransactionApi {

@GetMapping("/transactions")
@ApiResponse(responseCode = "200", description = "Transaction found")
ResponseEntity<List<UserTransactionDTO>> getTransactions();

@ApiResponse(responseCode = "404", description = "User not found")
@GetMapping("/transactions/user/{id}")
    ResponseEntity<List<TransactionDTO>> getTransactionByUserId(
            @PathVariable Long id);

@ApiResponse(responseCode = "404", description = "Transaction not found")
@PostMapping("/transactions/user/{userId}")
    ResponseEntity<List<TransactionDTO>> createTransactionForUserId(
        @PathVariable Long userId,
        @RequestBody TransactionRequestDTO);
}
