package casino.banking.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundExeption extends RuntimeException implements HttpStatusProvider {

    private final HttpStatus status =  HttpStatus.NOT_FOUND;

    public UserNotFoundExeption(Long id) {
        super("User with id " + id + " not found");
    }
    public UserNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
