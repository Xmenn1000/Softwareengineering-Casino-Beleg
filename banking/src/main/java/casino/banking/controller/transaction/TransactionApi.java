package casino.banking.controller.transaction;

import casino.banking.request.transaction.TransactionRequestDTO;
import casino.banking.request.transaction.UserGameTransactionRequestDTO;
import casino.banking.view.transaction.TransactionDTO;
import casino.banking.view.transaction.UserTransactionDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Transaction", description = "Manage Game Service Transactions")
@RequestMapping("/casino/bank/api")
@RestController
public interface TransactionApi {
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @GetMapping("/transactions")
    ResponseEntity<List<UserTransactionDTO>> findAll();

    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping("/transactions/user/{id}")
        ResponseEntity<List<TransactionDTO>> findByUserId(
            @PathVariable Long id);

    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @ApiResponse(responseCode = "400", description = "Invalid Request Body or unknown userId")
    @ApiResponse(responseCode = "201", description = "Transaction found")
    @PostMapping("/transaction/user/{userId}")
        ResponseEntity<UserTransactionDTO> createForUserId(
            @PathVariable Long userId,
            @RequestBody TransactionRequestDTO transactionRequestDTO);

    @ApiResponse(responseCode = "404", description = "User or transactionsId not found")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "200", description = "User  found")
    @PutMapping("/transaction/{transactionId}")
        ResponseEntity<UserTransactionDTO> replaceById(
            @PathVariable Long transactionId,
            @RequestBody UserGameTransactionRequestDTO userGameTransactionRequestDTO
    );

    @DeleteMapping("/transaction/{transactionId}")
        ResponseEntity<UserTransactionDTO> deleteById(
            @PathVariable Long transactionId
    );
}
