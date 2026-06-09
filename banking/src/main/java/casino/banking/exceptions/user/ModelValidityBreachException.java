package casino.banking.exceptions.user;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class ModelValidityBreachException extends HttpException {
    public ModelValidityBreachException(String message, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, message, cause);
    }

    public ModelValidityBreachException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
