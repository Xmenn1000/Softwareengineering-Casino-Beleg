package casino.banking.view.transaction;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserTransactionDTO {
    Long userID;

    @JsonUnwrapped
    TransactionDTO transactionDTO;
}