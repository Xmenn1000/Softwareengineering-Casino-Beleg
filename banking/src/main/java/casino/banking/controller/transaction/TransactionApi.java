package casino.banking.controller.transaction;

import casino.banking.util.GameService;
import casino.banking.view.transaction.request.TransactionRequestDTO;
import casino.banking.view.transaction.request.UserGameTransactionRequestDTO;
import casino.banking.view.transaction.request.UserTransactionRequestDTO;
import casino.banking.view.transaction.response.TransactionDTO;
import casino.banking.view.transaction.response.UserTransactionDTO;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.hibernate.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/casino/bank/api")
@RestController
public interface TransactionApi {
    @ApiResponse(responseCode = "200", description = "Transaction found")
    @GetMapping("/transactions")
    ResponseEntity<List<UserTransactionDTO>> getTransactions();

    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "200", description = "User found")
    @GetMapping("/transactions/user/{id}")
        ResponseEntity<List<TransactionDTO>> getTransactionByUserId(
            @PathVariable Long id);

    @ApiResponse(responseCode = "404", description = "Transaction not found")
    //TODO: 2 Reasons for one response code --> Not good!
    @ApiResponse(responseCode = "400", description = "Invalid Request Body or unknown userId")
    @ApiResponse(responseCode = "201", description = "Transaction found")
    @PostMapping("/transactions/user/{userId}")
        ResponseEntity<UserTransactionRequestDTO> createTransactionForUserId(
            @PathVariable Long userId,
            @RequestBody TransactionRequestDTO transactionRequestDTO);

    //TODO: 2 Reasons for one response code --> Not good!
    @ApiResponse(responseCode = "404", description = "User or transactionsId not found")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "200", description = "User  found")
    @PutMapping("/transactions/{transactionId}")
        ResponseEntity<UserTransactionRequestDTO> createTransactionIdForTransaction(
            @PathVariable Long transactionId,
            @RequestBody UserGameTransactionRequestDTO userGameTransactionRequestDTO
    );

    @DeleteMapping("/transactions/{transactionId}")
        ResponseEntity<UserGameTransactionRequestDTO> deleteTransactionByTransactionId(
            @PathVariable Long transactionId,
            @RequestBody UserTransactionRequestDTO userGameTransactionRequestDTO
    );




}