package casino.banking.exceptions.transaction;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class TransactionModelValidityBreachException extends HttpException {
   public TransactionModelValidityBreachException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

    public TransactionModelValidityBreachException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
