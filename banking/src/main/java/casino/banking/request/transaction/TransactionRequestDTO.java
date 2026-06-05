package casino.banking.request.transaction;

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
