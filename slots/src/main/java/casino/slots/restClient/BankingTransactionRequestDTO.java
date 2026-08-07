package casino.slots.restClient;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BankingTransactionRequestDTO {
    private String invoicingParty;
    private BigDecimal amount;
}
