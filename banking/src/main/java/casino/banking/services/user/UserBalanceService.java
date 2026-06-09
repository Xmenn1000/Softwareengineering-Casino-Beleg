package casino.banking.services.user;

import casino.banking.view.user.UserDTO;

import java.math.BigInteger;

public interface UserBalanceService {
    UserDTO depositBalanceById(Long userId, BigInteger amount, int decimals);
}
