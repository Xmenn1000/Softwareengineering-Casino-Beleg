package casino.banking.exceptions.user;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

import java.math.BigInteger;

public class BadDepositRequestException extends HttpException {

    public BadDepositRequestException(Long userId, BigInteger amount, int decimals) {
        super(HttpStatus.BAD_REQUEST, String.format("Could not deposit amount %s with decimals %d for user %d", amount, decimals, userId));
    }
}
