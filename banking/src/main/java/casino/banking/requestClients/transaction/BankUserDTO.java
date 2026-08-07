package casino.banking.requestClients.transaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class BankUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
}
