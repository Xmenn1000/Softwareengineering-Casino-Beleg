package casino.banking.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException implements ServiceExceptions {

    private final HttpStatus status =  HttpStatus.NOT_FOUND;

    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}