package casino.banking.request.transaction;

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