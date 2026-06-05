package casino.banking.view.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionDTO {
    Long id;
    BigDecimal amount;
}
