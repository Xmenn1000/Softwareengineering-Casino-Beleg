package casino.banking.view.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
@Data
public class UserDTO {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal balance;
}
