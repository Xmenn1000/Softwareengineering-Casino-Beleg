package casino.roulette.exceptions;

import org.springframework.http.HttpStatus;

public class BadRouletteRequestException extends HttpException {

    public BadRouletteRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
