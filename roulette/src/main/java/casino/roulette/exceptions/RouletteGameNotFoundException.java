package casino.roulette.exceptions;

import org.springframework.http.HttpStatus;

public class RouletteGameNotFoundException extends HttpException {

    public RouletteGameNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Roulette game with id " + id + " not found");
    }
}
