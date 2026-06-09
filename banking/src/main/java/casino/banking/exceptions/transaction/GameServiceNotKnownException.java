package casino.banking.exceptions.transaction;

import casino.banking.exceptions.HttpException;
import org.springframework.http.HttpStatus;

public class GameServiceNotKnownException extends HttpException {

    public GameServiceNotKnownException(String service) {
        super(HttpStatus.NOT_FOUND, "GameService " + service + " not found");
    }
}
