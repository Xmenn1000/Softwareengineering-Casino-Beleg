package casino.banking.exceptions;

import org.springframework.http.HttpStatus;

public class GameServiceNotKnownException extends RuntimeException implements HttpStatusProvider {

    private final HttpStatus status =  HttpStatus.NOT_FOUND;

    public GameServiceNotKnownException(String service) {
        super("GameService " + service + " not found");
    }
    public GameServiceNotKnownException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
