package casino.banking.view.transaction.request;

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