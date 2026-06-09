package casino.banking.exceptions.transaction;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class BadTransactionRequestException extends HttpException {
    public BadTransactionRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
