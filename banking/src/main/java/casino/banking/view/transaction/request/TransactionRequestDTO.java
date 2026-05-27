package casino.banking.view.transaction.request;

import casino.banking.util.GameService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionRequestDTO {
    GameService gameService;
    BigDecimal amount;
}
