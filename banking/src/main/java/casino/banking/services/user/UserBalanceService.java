package casino.banking.services.user;

import casino.banking.view.user.UserDTO;

import java.math.BigDecimal;

public interface UserBalanceService {
    UserDTO depositBalanceById(Long userId, BigDecimal amount, int decimals);
}
