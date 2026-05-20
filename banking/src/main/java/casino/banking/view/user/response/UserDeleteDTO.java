package casino.banking.view.user.response;

import java.math.BigDecimal;

public record UserDeleteDTO(
        String fistName,
        String lastName,
        BigDecimal balance) {
}
