package casino.banking.view.user.response;

import java.math.BigDecimal;

public record UserDTO(
        Long id,
        String fistName,
        String lastName,
        BigDecimal balance) {
}
