package casino.banking.view.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UserDeleteDTO {
    String fistName;
    String lastName;
    BigDecimal balance;
}
