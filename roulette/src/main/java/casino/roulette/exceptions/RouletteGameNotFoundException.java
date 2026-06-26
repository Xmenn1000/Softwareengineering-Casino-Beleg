package casino.roulette.exceptions;

public class RouletteGameNotFoundException extends RuntimeException {

    public RouletteGameNotFoundException(Long id) {
        super("Roulette game with id " + id + " not found");
    }
}
