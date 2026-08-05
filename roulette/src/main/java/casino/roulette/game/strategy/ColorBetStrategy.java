package casino.roulette.game.strategy;

import casino.roulette.exceptions.BadRouletteRequestException;
import casino.roulette.util.BetType;
import org.springframework.stereotype.Component;

@Component
public class ColorBetStrategy extends RouletteBetStrategySupport {

    @Override
    public BetType betType() {
        return BetType.COLOR;
    }

    @Override
    public boolean isWinning(String betValue, int ballPosition) {
        validateBallPosition(ballPosition);

        if (ballPosition == 0) {
            return false;
        }

        String normalizedBetValue = normalize(betValue);

        if (!normalizedBetValue.equals("RED") && !normalizedBetValue.equals("BLACK")) {
            throw new BadRouletteRequestException("COLOR bet value must be RED or BLACK");
        }

        boolean ballIsRed = isRedNumber(ballPosition);

        return normalizedBetValue.equals("RED") == ballIsRed;
    }

    @Override
    public int payoutMultiplier() {
        return 1;
    }

    @Override
    public int winningOutcomes() {
        return 18;
    }

    private boolean isRedNumber(int ballPosition) {
        return switch (ballPosition) {
            case 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 -> true;
            default -> false;
        };
    }
}
