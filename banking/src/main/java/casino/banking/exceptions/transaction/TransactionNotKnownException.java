package casino.banking.exceptions.transaction;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class TransactionNotKnownException extends HttpException {

    public TransactionNotKnownException(Long id) {
        super(HttpStatus.NOT_FOUND, "Transaction with id " + id + " not found");
    }

    public TransactionNotKnownException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }
}