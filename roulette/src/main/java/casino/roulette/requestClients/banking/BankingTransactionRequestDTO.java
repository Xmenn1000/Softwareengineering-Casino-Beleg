package casino.roulette.requestClients.banking;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BankingTransactionRequestDTO {
    private String gameService;
    private BigDecimal amount;
}
