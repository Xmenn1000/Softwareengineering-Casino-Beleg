package casino.banking.view.transaction;

import casino.banking.util.GameService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TransactionDeleteDTO {

    GameService invoicingParty;
    Long user;
    BigDecimal amount;
}
