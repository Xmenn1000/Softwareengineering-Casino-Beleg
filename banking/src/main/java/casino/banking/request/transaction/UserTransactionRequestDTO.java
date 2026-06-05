package casino.banking.request.transaction;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTransactionRequestDTO {
    Long userId;
    Long transactionId;

    @JsonUnwrapped
    TransactionRequestDTO transactionRequestDTO;
}