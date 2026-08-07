package casino.banking.exceptions.transaction;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class TransactionUserNotKnownException extends HttpException {

    public TransactionUserNotKnownException(Long userId) {
        super(HttpStatus.NOT_FOUND, "User with id " + userId + " not found");
    }

    public TransactionUserNotKnownException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }
}
