package casino.banking.exceptions.user;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class UserModelValidityBreachException extends HttpException {
    public UserModelValidityBreachException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

    public UserModelValidityBreachException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
