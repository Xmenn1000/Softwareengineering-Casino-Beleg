package casino.banking.handler.user;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundExeption extends RuntimeException implements HttpStatusProvider {

    private final HttpStatus status =  HttpStatus.NOT_FOUND;

    public CustomerNotFoundExeption(String message) {
        super(message);
    }
    public CustomerNotFoundExeption(Long id) {
        super("User with id " + id + " not found");
    }

    public CustomerNotFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
