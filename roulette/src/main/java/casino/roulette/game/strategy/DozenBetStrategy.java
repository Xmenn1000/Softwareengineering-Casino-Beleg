package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.springframework.stereotype.Component;

@Component
public class DozenBetStrategy extends RouletteBetStrategySupport {

    @Override
    public BetType betType() {
        return BetType.DOZEN;
    }

    @Override
    public boolean isWinning(String betValue, int ballPosition) {
        validateBallPosition(ballPosition);

        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("FIRST")
                && !normalizedBetValue.equals("SECOND")
                && !normalizedBetValue.equals("THIRD")) {
            throw new BadRouletteRequestException("DOZEN bet value must be FIRST, SECOND or THIRD");
        }

        return switch (normalizedBetValue) {
            case "FIRST" -> ballPosition >= 1 && ballPosition <= 12;
            case "SECOND" -> ballPosition >= 13 && ballPosition <= 24;
            case "THIRD" -> ballPosition >= 25 && ballPosition <= 36;
            default -> false;
        };
    }

    @Override
    public int payoutMultiplier() {
        return 2;
    }

    @Override
    public int winningOutcomes() {
        return 12;
    }
}
