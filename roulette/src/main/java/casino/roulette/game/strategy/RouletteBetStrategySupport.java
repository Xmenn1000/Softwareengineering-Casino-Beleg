package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;

abstract class RouletteBetStrategySupport implements RouletteBetStrategy {

    protected void validateBallPosition(int ballPosition) {
        if (ballPosition < 0 || ballPosition > 36) {
            throw new BadRouletteRequestException("Ball position must be between 0 and 36");
        }
    }

    protected String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new BadRouletteRequestException("Bet value must not be empty");
        }

        return value.trim().toUpperCase();
    }

    protected int parseBetNumber(String betValue) {
        String normalizedBetValue = normalize(betValue);

        try {
            return Integer.parseInt(normalizedBetValue);
        } catch (NumberFormatException ex) {
            throw new BadRouletteRequestException("Bet value must be a number");
        }
    }
}
