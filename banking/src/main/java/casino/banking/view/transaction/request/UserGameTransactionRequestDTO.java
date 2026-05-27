package casino.banking.view.transaction.request;

import casino.banking.util.GameService;
import casino.banking.view.transaction.response.TransactionDTO;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserGameTransactionRequestDTO {
    Long userId;

    @JsonUnwrapped
    TransactionRequestDTO transactionRequestDTO;

}