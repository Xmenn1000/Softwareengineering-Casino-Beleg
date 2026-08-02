package casino.slots.restClient;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BankingTransactionRequestDTO {
    private String gameService;
    private BigDecimal amount;
}
