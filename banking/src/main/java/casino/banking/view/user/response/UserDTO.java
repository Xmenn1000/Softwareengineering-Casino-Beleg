package casino.banking.view.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class UserDTO {
    Long id;
    String fistName;
    String lastName;
    BigDecimal balance;
}
