package casino.banking.view.transaction.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionDTO {
    Long id;
    BigDecimal amount;
}
