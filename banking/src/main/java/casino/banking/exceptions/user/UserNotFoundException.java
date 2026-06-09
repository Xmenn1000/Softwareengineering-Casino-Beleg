package casino.banking.exceptions.user;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends HttpException {

    private final HttpStatus status =  HttpStatus.NOT_FOUND;

    public UserNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "User with id " + id + " not found");
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(HttpStatus.NOT_FOUND, message, cause);
    }

    @Override
    public HttpStatus getHTTPStatus() {
        return status;
    }
}